/*회원가입 모달창*/
import React, { useState } from "react";
import styled, { keyframes } from "styled-components";
import { Link } from "react-router-dom";
import xButton from "../../images/svg/close-icon.svg";
//모달창
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

const SignInModalBg = styled.div`
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

const SignInModalBox = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-evenly;
  width: 480px;
  height: 266px;
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
    font-size: 16px;
  }
  p:first-of-type {
    margin-top: 25px;
  }
  p:nth-of-type(2) {
    margin-bottom: 40px;
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

const ButtonS = styled.button`
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
    background-color: rgba(255, 206, 27, 1);
    color: var(--white);
    font-family: "Pretendard-Regular";
  }
`;

const SignInCloseButton = styled.button`
  position: absolute;
  top: 20px;
  right: 20px;
  background-color: transparent;
  border: none;
  cursor: pointer;
  font-size: 1.5rem;
`;

const ModalSignIn = ({ closeModal, isModalOpen }) => {
  const [isClosing, setIsClosing] = useState(false); // 닫힐때 애니메이션
  const handleOk = () => {
    setIsClosing(true);
    setTimeout(() => {
      closeModal();
      setIsClosing(false);
    }, 200);
  };

  return (
    <SignInModalBg>
      <ModalAnimation isOpen={isModalOpen}>
        <SignInModalBox className={isClosing ? "fade-out" : ""}>
          <SignInCloseButton onClick={handleOk}>
            <img src={xButton} alt="Close" />
          </SignInCloseButton>
          <h1>사용자 역할 선택</h1>
          <p>가족의 건강을 지원하려면 보호자를 선택하세요.</p>
          <p>전문 관리 기능을 사용하려면 재활사를 선택하세요.</p>
          <div>
            <Link to={"/sign-in-nok"}>
              <ButtonS onClick={handleOk}>보호자입니다</ButtonS>
            </Link>
            <Link to={"/sign-in-path"}>
              <ButtonS onClick={handleOk}>재활사입니다</ButtonS>
            </Link>
          </div>
        </SignInModalBox>
      </ModalAnimation>
    </SignInModalBg>
  );
};

export default ModalSignIn;
