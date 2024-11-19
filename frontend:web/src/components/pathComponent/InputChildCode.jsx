/*(재활사입장) 아동 코드 입력 모달창*/
import React, { useState } from "react";
import styled, { keyframes } from "styled-components";
import xButton from "../../images/svg/close-icon.svg";
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

const ModalBg = styled.div`
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

const ModalBox = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-evenly;
  width: 480px;
  height: 309px;
  background-color: rgba(247, 246, 245, 1);
  border-radius: 15px;
  position: relative;
  box-shadow: 1px 3px 5px rgba(0, 0, 0, 0.2);

  h2 {
    font-size: 32px;
    font-family: "Pretendard-Bold";
    margin-top: 40px;
    margin-bottom: 29px;
  }
  p {
    font-family: "Pretendard-Regular";
    font-size: 16px;
    margin-bottom: 29px;
  }
  div {
    display: flex;
    justify-content: space-between;
    width: 350px;
    height: 105px;
    margin-bottom: 40px;
  }
  &.fade-out {
    animation: ${ModalfadeOut} 0.1s ease-out forwards;
  }
`;

const Button = styled.button`
  background-color: rgba(255, 206, 27, 1);
  color: rgba(17, 17, 17, 1);
  cursor: pointer;
  font-size: 16px;
  border: none;
  width: 350px;
  height: 40px;
  font-family: "Pretendard-Bold";
  transition: background-color 0.3s ease;
  &:hover {
    background-color: rgba(255, 173, 0, 1);
    color: rgba(17, 17, 17, 1);
  }
`;

const CloseButton = styled.button`
  position: absolute;
  top: 0px;
  right: 0px;
  margin: 20px;
  background-color: transparent;
  border: none;
  cursor: pointer;
  font-size: 1.5rem;
`;

const ChildCodeInput = styled.div`
  display: flex;
  flex-direction: column;
  input {
    /* input 스타일 */
    width: 345px;
    height: 36px;
    margin-top: 0px;
    border: 1px solid rgba(255, 206, 27, 1);
    background-color: rgba(255, 253, 234, 1);
    font-size: 12px;
    font-family: "Pretendard-Regular";
    box-shadow: none;
    outline: none;
    &:hover {
      box-shadow: none;
      background-color: rgba(255, 239, 133, 1);
      outline: none;
    }
    &:focus {
      background-color: var(white);
    }
  }
`;

const InputChildCode = ({ closeModal, linkChild, isModalOpen }) => {
  const [childCode, setChildCode] = useState("");
  const [isClosing, setIsClosing] = useState(false); // 닫힐때 애니메이션
  const handleConfirm = async () => {
    try {
      await linkChild(childCode);
      setIsClosing(true); // 서버에 데이터 전송 후 모달 닫기
      setTimeout(() => {
        closeModal();
        setIsClosing(false);
      }, 200);
    } catch (error) {
      console.error("아동 코드 입력 실패:", error);
    }
  };
  const handleClosedButton = () => {
    setIsClosing(true);
    setTimeout(() => {
      closeModal();
      setIsClosing(false);
    }, 200);
  };

  const handleChange = (e) => {
    setChildCode(e.target.value);
  };

  return (
    <ModalBg>
      <ModalAnimation isOpen={isModalOpen}>
        <ModalBox className={isClosing ? "fade-out" : ""}>
          <CloseButton onClick={handleClosedButton}>
            <img src={xButton} alt="Close" />
          </CloseButton>
          <h2>아동 코드 입력</h2>
          <p>연동을 위해 아동 코드를 입력해주세요</p>
          <ChildCodeInput>
            <input
              id="child-code"
              type="text"
              placeholder="아동코드(5자) 입력"
              value={childCode}
              onChange={handleChange}
            ></input>
            <Button onClick={handleConfirm}>확인</Button>
          </ChildCodeInput>
        </ModalBox>
      </ModalAnimation>
    </ModalBg>
  );
};

export default InputChildCode;
