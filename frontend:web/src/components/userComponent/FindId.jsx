/*아이디 찾기 창*/
import React, { useState } from "react";
import styled from "styled-components";
import backButton from "../../images/svg/close-icon.svg";
//import backButton from "../../images/svg/left-arrow.svg";
import axios from "axios";
import ViewFindID from "./ViewFindID";

const FindIdModalBg = styled.div`
  display: flex;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 9999;
  align-items: center;
  justify-content: center;
  width: 100vw;
  height: 100vh;
  background-color: transparent;
`;

const FindIdModalBox = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-evenly;
  width: 480px;
  height: 242px;
  background-color: #f7f6f5;
  border-radius: 15px;
  position: relative;
  box-shadow: 1px 3px 5px rgba(0, 0, 0, 0.2);

  h2 {
    font-size: 25px;
    height: 10px;
    font-family: "Pretendard-Bold";
    margin-top: -10px;
    margin-bottom: 0px;
  }
  div {
    display: flex;
    justify-content: space-between;
    width: 290px;
    margin-top: 0px;
    margin-bottom: 0px;
  }
`;
//확인 버튼
const FindIdButton = styled.button`
  background-color: rgba(255, 206, 27, 1);
  color: rgba(17, 17, 17, 1);
  cursor: pointer;
  font-size: 14px;
  border: none;
  width: 285px;
  height: 30px;
  margin-top: 20px;
  transition: background-color 0.3s ease;
  &:hover {
    background-color: rgba(255, 173, 0, 1);
    color: rgba(17, 17, 17, 1);
  }
  font-family: "Pretendard-Bold";
`;

const FindIdCloseButton = styled.button`
  position: absolute;
  top: 10px;
  right: 10px;
  background-color: transparent;
  border: none;
  cursor: pointer;
  font-size: 1.5rem;
`;

const FindIdInput = styled.div`
  display: flex;
  flex-direction: column;
  margin-top: 10px;

  input {
    /* input 스타일 */
    outline: none;
    width: 280px;
    height: 30px;
    margin: 3px 0px;
    border: 1px solid #rgba(255, 253, 234, 1);
    font-size: 12px;
    font-family: "Pretendard-Regular";
    box-shadow: none;
    background-color: var(--white);
    &:hover {
      background-color: rgba(255, 239, 133, 1);
    }
    &:focus {
      background-color: rgba(255, 253, 234, 1);
    }
  }
`;

const FindId = ({ closeModal }) => {
  const [userName, setUserName] = useState("");
  const [userEmail, setUserEmail] = useState("");
  const [loading, setLoading] = useState(false); // 아이디 찾는 중<<로딩창용
  const [userId, setUserId] = useState("");
  const [showIdModal, setShowIdModal] = useState(false); //view id 모달창

  //둘 중 하나라도 입력하지 않았을 경우 경고창
  const handleFindID = async () => {
    if (!userName || !userEmail) {
      alert("모두 입력하세요!");
      return;
    }
    setLoading(true);
    //get 요청
    try {
      const response = await axios.get(
        `${process.env.REACT_APP_SERVER_IP}/user/find-id?`,
        {
          params: {
            name: userName,
            email: userEmail,
          },
        }
      );
      if (response.status === 200) {
        setUserId(response.data); //서버에서 받은 아이디 저장
        setShowIdModal(true);
      } else {
        alert("아이디 찾기 실패! 아이디와 이메일을 다시 확인하세요.");
      }
    } catch (error) {
      alert("아이디 찾기 실패! 아이디와 이메일을 다시 확인하세요.");
      console.log("아이디 찾기 오류");
      console.error(error);
    } finally {
      setLoading(false); // 로딩 상태 해제
    }
  };

  return (
    <>
      <FindIdModalBg>
        <FindIdModalBox>
          <FindIdCloseButton onClick={closeModal}>
            <img src={backButton} alt="Close" />
          </FindIdCloseButton>
          <h2>아이디 찾기</h2>
          <FindIdInput>
            <input
              type="text"
              placeholder="등록한 이름을 입력하세요"
              value={userName}
              onChange={(e) => setUserName(e.target.value)} //이름 입력값
            ></input>
            <input
              type="text"
              placeholder="등록한 이메일을 입력하세요"
              value={userEmail}
              onChange={(e) => setUserEmail(e.target.value)} //이메일 입력값
            ></input>
            <FindIdButton onClick={handleFindID} disabled={loading}>
              {loading ? "아이디 확인 중..." : "확인"}
            </FindIdButton>
          </FindIdInput>
        </FindIdModalBox>
      </FindIdModalBg>
      {showIdModal && (
        <ViewFindID
          userId={userId}
          userName={userName}
          closeModal={closeModal} // 모달 닫는 함수
        />
      )}
    </>
  );
};

export default FindId;
