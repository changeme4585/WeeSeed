/*로그아웃 모달창*/
import React, { useState } from "react";
import styled, { keyframes } from "styled-components";
import xButton from "../../images/svg/close-icon.svg";
import { useNavigate } from "react-router-dom";
import ModalAnimation from "../AnimationComponent/ModalAnimation";

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

const LogoutModalBg = styled.div`
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

const LogoutModalBox = styled.div`
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

  h1 {
    font-size: 32px;
    font-family: "Pretendard-Bold";
    margin-top: 40px;
  }
  p {
    font-family: "Pretendard-Regular";
    margin-top: 25px;
    margin-bottom: 40px;
    font-size: 16px;
  }
  div {
    display: flex;
    justify-content: space-between;
    width: 300px;
    height: 36px;
    margin-bottom: 40px;
  }
  &.fade-out {
    animation: ${ModalfadeOut} 0.1s ease-out forwards;
  }
`;

const LogoutButton = styled.button`
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
    background-color: rgba(255, 173, 0, 1);
    color: rgba(17, 17, 17, 1);
    font-family: "Pretendard-Regular";
  }
`;

const LogoutButtonC = styled.button`
  background-color: #d9d4cf;
  color: rgba(17, 17, 17, 1);
  cursor: pointer;
  font-size: 16px;
  border: none;
  width: 144px;
  height: 36px;
  font-family: "Pretendard-Regular";
  transition: background-color 0.3s ease;
  &:hover {
    background-color: #b6aea5;
    color: rgba(17, 17, 17, 1);
    font-family: "Pretendard-Regular";
  }
`;

const LogoutCloseButton = styled.button`
  position: absolute;
  top: 20px;
  right: 20px;
  background-color: transparent;
  border: none;
  cursor: pointer;
  font-size: 1.5rem;
`;

const LogoutAlert = ({ closeModal, isModalOpen }) => {
  const navigate = useNavigate();
  const [isClosing, setIsClosing] = useState(false); // 닫힐때 애니메이션
  const handleOk = () => {
    setIsClosing(true);
    setTimeout(() => {
      closeModal();
      setIsClosing(false);
    }, 200);
  };

  //로그아웃처리
  const handleLogout = async () => {
    localStorage.clear();
    navigate("/");
    try {
      const response = await fetch(
        `${process.env.REACT_APP_SERVER_IP}/logout`,
        {
          method: "POST",
          credentials: "include",
        }
      );
      const data = await response.text();
      if (response.ok) {
        console.log("로그아웃 성공");
        localStorage.clear();
        navigate("/");
      } else {
        console.log("로그아웃 실패:", data);
      }
    } catch (error) {
      console.error("로그아웃 에러:", error);
    }
  };

  return (
    <LogoutModalBg>
      <ModalAnimation isOpen={isModalOpen}>
        <LogoutModalBox className={isClosing ? "fade-out" : ""}>
          <LogoutCloseButton onClick={handleOk}>
            <img src={xButton} alt="Close" />
          </LogoutCloseButton>
          <h1>로그아웃 하시겠습니까?</h1>
          <p>확인을 누르면 로그아웃됩니다.</p>
          <div>
            <LogoutButtonC onClick={handleOk}>취소</LogoutButtonC>
            <LogoutButton onClick={handleLogout}>확인</LogoutButton>
          </div>
        </LogoutModalBox>
      </ModalAnimation>
    </LogoutModalBg>
  );
};

export default LogoutAlert;
