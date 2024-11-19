import React, { useState, useEffect, useCallback, useContext, useRef } from "react";
import "../styles/Settings.css";
import modifyImage from "../images/svg/modify-icon.svg"; //수정 아이콘
import profilePathImage from "../images/basicImg/재활사 프로필.png"; //재활사용 프로필 그림
import profileNokImage from "../images/basicImg/보호자 프로필.png"; //보호자용 프로필 그림

import axios from "axios";
import AddChildListItem from "../components/childComponent/AddChildListItem"; //아동 추가 form
import AddChildButton from "../components/nokComponent/AddChildButton";
import LinkChildButton from "../components/pathComponent/LinkChildButton"; //재활사 (아동 연결)
import ResignConfirm from "../components/userComponent/ResignConfirm"; //탈퇴 확인창
import ChildListItem from "../components/childComponent/ChildListItem";
import { ChildContext } from "../index";
import NotFoundImg from "../images/svg/error-icon.svg";
import {useNavigate} from "react-router-dom"; //404 그림

import chatIcon from "../images/basicImg/chat-icon.png"; //gpt 연동용 챗 아이콘..
import chatClose from "../images/svg/close-icon.svg"; // 챗 닫기 버튼
import chatSend from "../images/basicImg/chat-send.png"; // 챗 보내기버튼
import chatAnalyze from "../images/basicImg/chat-analyze.png"; // 음성 정확도 gpt한테 보내는 버튼
import LoadingAnimation from "../components/AnimationComponent/LoadingAnimation";

function Settings() {
  const chatDisplayRef = useRef(null); //스크롤 자동 이동
  const { selectedChild, setSelectedChild } = useContext(ChildContext);
  const [userData, setUserData] = useState(null);
  const [isAddChild, setIsAddChild] = useState(false); // 보호자일 경우, 아동 추가
  const [editedData, setEditedData] = useState({}); // 수정된 데이터 상태
  const [isEditMode] = useState(false); // 회원 정보 수정 모드
  const [showConfirmModal, setShowConfirmModal] = useState(false); //탈퇴시 확인창
  const userType = localStorage.getItem("userType");
  const userId = localStorage.getItem("userId");
  const profileImage = userType === "Path" ? profilePathImage : profileNokImage; //프로필
  const navigate = useNavigate();
  const [chatOpen, setChatOpen] = useState(false); //채팅창 on off 상태
  const [input, setInput] = useState("");
  const [isLoading, setIsLoading] = useState(false); //응답 전 로딩 애니메이션 띄우기 위한 상태
  const [messages, setMessages] = useState([]); //메시지 상태

  useEffect(() => {
    if (chatDisplayRef.current) {
      chatDisplayRef.current.scrollTop = chatDisplayRef.current.scrollHeight;
    }
  }, [messages]);
  /*
  //회원 정보 edit 모드 -> 일단 기본 정보부터 불러오기
  const handleModifyInfo = () => {
    setIsEditMode(true); // 바로 수정 모드로 전환
    setEditedData({
      name: userData.name,
      email: userData.email,
      organizationName: userData.organizationName || "",
    });
  };
   */

  //보호자가 아동 추가 버튼 눌렀을때
  const handleAddChildClick = () => {
    console.log("아동 추가하기 버튼 클릭됨!!");
    setIsAddChild(true);
    console.log("isAddChild 상태:", isAddChild);
  };

  //아동 추가시
  const handleChildAdded = (newChild) => {
    setUserData((prevData) => ({
      ...prevData,
      child: [...(prevData.child || []), newChild], //새로 추가된 아동 업데이트
    }));
    setIsAddChild(false);
  };

  //사용자가 정보 수정 input에 입력하는 값들 처리
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setEditedData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };
  const handleResignUser = () => {
    setShowConfirmModal(true); // 탈퇴 확인 팝업창 띄우기
  };

  //아동 목록 불러오기
  const fetchChildData = useCallback(async () => {
    try {
      let response;
      if (userType === "Path") {
        response = await axios.get(
          `${process.env.REACT_APP_SERVER_IP}/PathChildInfo/${userId}`
        );
      } else if (userType === "Nok") {
        response = await axios.get(
          `${process.env.REACT_APP_SERVER_IP}/NokChildInfo/${userId}`
        );
      } else {
        console.log("유효하지 않은 유저 타입입니다.");
        return;
      }
      const child = response.data;
      setUserData((prevData) => ({
        ...prevData,
        child,
      }));
      localStorage.setItem("child", JSON.stringify(child));
    } catch (error) {
      console.error("아동 정보를 불러오는 중 오류 발생:", error);
    }
  }, [userId, userType]);

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        let userInfoResponse;
        if (userType === "Path") {
          userInfoResponse = await axios.get(
            `${process.env.REACT_APP_SERVER_IP}/user/pathologist/${userId}`
          );
        } else if (userType === "Nok") {
          userInfoResponse = await axios.get(
            `${process.env.REACT_APP_SERVER_IP}/user/nok/${userId}`
          );
        } else {
          console.log("유효하지 않은 유저 타입입니다.");
          return;
        }
        setUserData(userInfoResponse.data);
        fetchChildData();
      } catch (error) {
        console.error("유저 정보를 불러오는 중 오류 발생:", error);
      }
    };

    fetchUserData();
  }, [userType, userId, fetchChildData]);

  // handleChildSelect 함수 정의
  const handleChildSelect = (child) => {
    setSelectedChild(child);
    localStorage.setItem("selectedChild", JSON.stringify(child));
    localStorage.setItem("selectedChildCode", child.childCode);
    console.log("설정에서 선택한 아동 코드:", child.childCode);
    navigate("/aac"); // 말해봐요 화면으로 변경
  };

  /*** 여기서부터 채팅 관련 기능!! ***/
  const toggleChat = () => {
    setChatOpen((prev) => !prev);
    if (chatOpen) {
      setMessages([]); //채팅 초기화~!
    }
  };

  //채팅 send 함수!!!
  const handleSendChat = async () => {
    if (!input.trim()) return; //빈 문자열 방지
    const userMessage = { type: "user", text: input };
    setMessages((prev) => [...prev, userMessage]);
    setInput(""); // 입력창 초기화
    setIsLoading(true); // 로딩 애니메이션 on
    setMessages((prev) => {
      const loadingMessage = { type: "gpt", text: <LoadingAnimation /> };
      const updatedMessages = [...prev, loadingMessage];
      return updatedMessages;
    });
    //get 요청.....
    try {
      const gptResponse = await axios.get(
          `${process.env.REACT_APP_SERVER_IP}/gpt_test`,
          {
            params: { gpt: input },
          }
      );
      //일단 콘솔 로그해
      console.log("Gpt의 답변:", gptResponse);
      //gpt 응답 메시지 처리..
      setMessages((prev) =>
          prev.map((msg, index) =>
              index === prev.length - 1
                  ? {
                    type: "gpt",
                    text: gptResponse.data.split('\n').map((line, i) => (
                        <p key={i}>{line}</p>
                    ))
                  }
                  : msg
          )
      );
    } catch (error) {
      console.error("채팅 보내기 실패...;;:", error);
      setMessages((prev) =>
          prev.map((msg, index) =>
              index === prev.length - 1
                  ? { type: "gpt", text: "응답에 실패했습니다. 다시 시도해주세요." }
                  : msg
          )
      );
    } finally {
      setIsLoading(false);
    }
  };
  //아동 발음 정확도 불러오기 함수
  const handleSpeechAccuracy = async () => {
    //1차적으로 아동 선택되어 있는지 확인
    if (!selectedChild) {
      alert("아동을 선택해주세요!");
      return;
    }
    console.log("선택된 아동의 코드:", selectedChild.childCode);
    //선택된 아동 있다면 제출 확인창 띄우기
    const confirmMsg = window.confirm(
        "아동의 발음 정확도 정보를 전송하시겠습니까?"
    );
    if (!confirmMsg) return;
    setIsLoading(true); //로딩 애니메이션 on
    const loadingMessage = { type: "gpt", text: <LoadingAnimation /> };
    setMessages((prev) => [...prev, loadingMessage]);
    const loadingIndex = messages.length;
    try {
      const accuracyResponse = await axios.get(
          `${process.env.REACT_APP_SERVER_IP}/feed-back`,
          {
            params: {
              childCode: selectedChild.childCode,
            },
          }
      );
      console.log("발음 정확도에 대한 GPT의 답변:", accuracyResponse.data);
      //그러면 받아온 gpt의 응답을 ui에 나타내줘야함
      setMessages((prev) =>
          prev.map((msg, index) =>
              index === loadingIndex
                  ? {
                    type: "gpt",
                    text: accuracyResponse.data.split('\n').map((line, i) => (
                        <p key={i}>{line}</p>
                    ))
                  }
                  : msg
          )
      );
    } catch (error) {
      console.error("발음정확도 불러오기 실패...", error);
    } finally {
      setIsLoading(false); //애니메이션 종료
    }
  };

  return (
    <>
      <div className="setting-root">
        <div className="profile-div">
          <h3 className="set-text">프로필</h3>
          <div className="profile-card">
            {userData && (
                <img
                    src={modifyImage}
                    alt="수정 아이콘"
                    className="modifyImg"
                    //onClick={handleModifyInfo} //버튼 클릭시 수정 모드로 전환
                />
            )}
            <div className="profile-img">
              <img
                  src={profileImage}
                  alt={userType === "Path" ? "재활사 프로필" : "보호자 프로필"}
              />
            </div>
            {userData ? (
                <>
                  <div className="user-info-content">
                    <div className="user-info">
                      <span>이름</span>
                      <span>이메일</span>
                      {userType === "Path" && <span>소속기관</span>}
                    </div>
                    <div className="user-real-info">
                      {isEditMode ? (
                          //수정 모드
                          <>
                            <input
                                name="name"
                                value={editedData.name}
                                onChange={handleInputChange}
                                className="user-edit-mode"
                            />
                            <input
                                name="email"
                                value={editedData.email}
                                onChange={handleInputChange}
                                className="user-edit-mode"
                            />
                            {userType === "Path" && (
                                <input
                                    name="organizationName"
                                    value={editedData.organizationName}
                                    onChange={handleInputChange}
                                    className="user-edit-mode"
                                />
                            )}
                          </>
                      ) : (
                          //기본 모드
                          <>
                            <span>{userData.name || " "}</span>
                            <span>{userData.email || " "}</span>
                            {userType === "Path" && (
                                <span>{userData.organizationName || " "}</span>
                            )}
                          </>
                      )}
                    </div>
                  </div>
                  <p
                      className="unregister-btn"
                      onClick={handleResignUser}
                      style={{marginTop: userType === "Nok" ? "85px" : "50px"}}
                  >
                    탈퇴하기
                  </p>
                </>
            ) : (
                <>
                  <div className="no-login">
                    <p className="no-login-p">유저 정보가 없습니다.</p>
                    <p className="no-login-p">프로필을 보려면 로그인 하세요.</p>
                  </div>
                </>
            )}
          </div>
        </div>
        <div className="child-list-div">
          <div className="child-list-header">
            <h3 className="set-text-child">아동 목록</h3>
            {userData &&
                (userType === "Nok" ? (
                    <AddChildButton onClick={handleAddChildClick}/>
                ) : (
                    <LinkChildButton fetchChildData={fetchChildData}/>
                ))}
          </div>

          <div className="child-list">
            {/* AddChildListItem을 표시할지 여부 확인 */}
            {isAddChild && (
                <AddChildListItem
                    onChildAdded={handleChildAdded} //아동이 추가되면 호출
                />
            )}

            {userData && userData.child ? (
                userData.child.map((child, index) => (
                    <ChildListItem
                        key={index}
                        child={child}
                        index={index}
                        onSelectChild={handleChildSelect} //handleChildSelect 함수를 props로 전달
                    />
                ))
            ) : (
                <p className="no-child-info">
                  <img src={NotFoundImg} alt="연동된 아동 없음"/>
                </p>
            )}
          </div>
        </div>
        <button
            className={`chat-icon-btn ${chatOpen ? "shrink" : "visible"}`}
            onClick={toggleChat}
        >
          <img src={chatIcon} alt="챗봇 아이콘"></img>
        </button>
        <div className={`chat-div ${chatOpen ? "visible" : ""}`}>
          <div className="chat-top-div">
            <button className="chat-analyze-btn" onClick={handleSpeechAccuracy}>
              <img src={chatAnalyze} alt="발음 정확도 gpt 통해 분석"></img>
            </button>
            <button className="chat-close-btn" onClick={toggleChat}>
              <img src={chatClose} alt="챗봇 닫기"></img>
            </button>
          </div>
          <div className="chatting">
            {/*채팅 내용 띄우기 주의:받은 채팅 크기에 따라 크기가 변해야함*/}
            <div className="chat-display" ref={chatDisplayRef}>
              {messages.map((msg, index) => (
                  <div key={index} className={`message ${msg.type}`}>
                    {msg.text}
                  </div>
              ))}
            </div>
            <div className="chat-input-div">
              <input
                  className="chat-input"
                  type="text"
                  placeholder="채팅을 입력하세요."
                  value={input}
                  onChange={(e) => setInput(e.target.value)}
                  onKeyDown={(e) => {
                    if (e.key === "Enter") {
                      handleSendChat();
                    }
                  }}
              />
              <button className="send-btn" onClick={handleSendChat}>
                <img src={chatSend} alt="채팅 보내기"></img>
              </button>
            </div>
          </div>
        </div>
        {showConfirmModal && (
            <ResignConfirm closeModal={() => setShowConfirmModal(false)}/>
        )}
      </div>
    </>
  );
}

export default Settings;
