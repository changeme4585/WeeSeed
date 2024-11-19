/*아이디 비번 찾기 창*/
import React, { useState } from "react";
import styled, { keyframes } from "styled-components";
import xButton from "../../images/svg/close-icon.svg";
import FindPw from "./FindPw";
import FindId from "./FindId";
import ViewFindID from "./ViewFindID";

// 창 열릴 때 애니메이션
const ModalfadeIn = keyframes`
  from {
    opacity: 0;
    scale: 0.95;
  }
  to {
    opacity: 1;
    scale: 1;
  }
`;

//창 닫힐때 애니메이션
const ModalfadeOut = keyframes`
  from {
    opacity: 1;
    scale: 1;
  }
  to {
    opacity: 0;
    scale: 0.99;
  }
`;

const FindIdPwModalBg = styled.div`
  display: flex;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 9999;
  align-items: center;
  justify-content: center;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.6);
`;

const FindIdPwModalBox = styled.div`
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
  animation: ${({ isClosing }) => (isClosing ? ModalfadeOut : ModalfadeIn)} 0.3s
    ease-out forwards;

  h1 {
    font-size: 32px;
    font-family: "Pretendard-Bold";
    margin-top: 40px;
    margin-bottom: 0px;
  }

  div {
    display: flex;
    justify-content: space-between;
    width: 300px;
    height: 36px;
    margin-bottom: 35px;
    font-family: "Pretendard-Regular";
    font-size: 16px;
    margin-top: 15px;
  }
`;

const ButtonID = styled.button`
  background-color: rgba(255, 206, 27, 1);
  color: rgba(17, 17, 17, 1);
  font-size: 16px;
  border: none;
  width: 144px;
  height: 36px;
  font-family: "Pretendard-Regular";
  transition: background-color 0.3s ease;
  &:hover {
    cursor: pointer;
    background-color: rgba(255, 206, 27, 1);
    color: var(--white);
    font-family: "Pretendard-Regular";
  }
`;

const ButtonPW = styled.button`
  background-color: rgba(255, 206, 27, 1);
  color: rgba(17, 17, 17, 1);
  cursor: pointer;
  font-size: 16px;
  border: none;
  width: 144px;
  height: 36px;
  font-family: "Pretendard-Regular";
  transition: background-color 0.3s ease;
  &:hover {
    cursor: pointer;
    background-color: rgba(255, 206, 27, 1);
    color: var(--white);
    font-family: "Pretendard-Regular";
  }
`;

const FindIdPwCloseButton = styled.button`
  position: absolute;
  top: 20px;
  right: 20px;
  background-color: transparent;
  border: none;
  cursor: pointer;
  font-size: 1.5rem;
`;

const FindIdPw = ({ closeModal }) => {
  const [showFindIdPwModal, setShowFindIdPwModal] = useState(true);
  const [showFindIdModal, setShowFindIdModal] = useState(false); //아이디 찾기창
  const [showFindPwModal, setShowFindPwModal] = useState(false); //비번 찾기창
  const [showViewFindIDModal, setShowViewFindIDModal] = useState(false); // 아이디 찾은 결과 창
  const [userId, setUserId] = useState(""); // 찾은 아이디 저장
  const [userName, setUserName] = useState(""); // 사용자 이름 저장

  //아이디 찾기 모달창 표시 함수
  const openFindIdModal = () => {
    setShowFindIdModal(true);
  };
  // 비밀번호 찾기 모달창 표시 함수
  const openFindPwModal = () => {
    setShowFindPwModal(true);
  };

  // 아이디 찾은 결과 창을 열고, FindIdPw 창 닫기
  const openViewFindIDModal = (userId, userName) => {
    setUserId(userId);
    setUserName(userName);
    setShowViewFindIDModal(true);
    setShowFindIdModal(false);
  };

  // FindIdPw 창 닫기 함수
  const closeFindIdPwModal = () => {
    setShowFindIdPwModal(false); // FindIdPw 창 닫기
    setShowFindIdModal(false);
    setShowFindPwModal(false);
    setShowViewFindIDModal(false);
  };

  return (
    <>
      {showFindIdPwModal && (
        <FindIdPwModalBg>
          <FindIdPwModalBox>
            <FindIdPwCloseButton onClick={closeModal}>
              <img src={xButton} alt="Close" />
            </FindIdPwCloseButton>
            <h1>찾고자 하는 정보를 선택하세요</h1>
            <div>
              <ButtonID onClick={openFindIdModal}>아이디 찾기</ButtonID>
              <ButtonPW onClick={openFindPwModal}>비밀번호 찾기</ButtonPW>
            </div>
          </FindIdPwModalBox>
        </FindIdPwModalBg>
      )}

      {showFindIdModal && ( // FindId 모달 표시
        <FindId
          closeModal={() => setShowFindIdModal(false)}
          openViewFindIDModal={openViewFindIDModal}
        />
      )}

      {showFindPwModal && ( // FindPw 모달 표시
        <FindPw closeModal={() => setShowFindPwModal(false)} />
      )}

      {showViewFindIDModal && ( // ViewFindID 모달 표시
        <ViewFindID
          closeModal={closeFindIdPwModal}
          userId={userId}
          userName={userName}
        />
      )}
    </>
  );
};

export default FindIdPw;
