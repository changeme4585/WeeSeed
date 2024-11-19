/*AAC ai 테스트 통과 or 실패시 뜨는 팝업창 */
import React, { useState } from "react";
import styled, { keyframes } from "styled-components";
import xButton from "../../images/svg/close-icon.svg"; //닫기 버튼
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

const AiTestModalBg = styled.div`
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

const AiTestModalBox = styled.div`
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

const AiTestButton = styled.button`
  background-color: rgba(255, 206, 27, 1);
  color: rgba(17, 17, 17, 1);
  cursor: pointer;
  font-size: 16px;
  border: none;
  width: 350px;
  height: 40px;
  font-family: "Pretendard-Regular";
  transition: background-color 0.3s ease;
  &:hover {
    background-color: rgba(255, 173, 0, 1);
    color: rgba(17, 17, 17, 1);
    font-family: "Pretendard-Regular";
  }
`;

const AiTestCloseButton = styled.button`
  position: absolute;
  top: 20px;
  right: 20px;
  background-color: transparent;
  border: none;
  cursor: pointer;
  font-size: 1.5rem;
`;

const detectedObjects = ['악세사리', '옷', '신발', '집', '씻기', '운동'];

const AiTestModal = ({ closeModal, isModalOpen, testResult, setCardName }) => {
    const [isClosing, setIsClosing] = useState(false); //닫힐때 애니메이션

    const handleOk = () => {
        if (detectedObjects.includes(testResult)) {
            setCardName(testResult);
        }
        setIsClosing(true);
        setTimeout(() => {
            closeModal();
            setIsClosing(false);
        }, 200);
    };

    const handleClose = () => {
        setIsClosing(true);
        setTimeout(() => {
            closeModal();
            setIsClosing(false);
        }, 200);
    };

    //ai 테스트 결과에 따라 문구 바꾸기
    const testResultH1 = () => {
        if (detectedObjects.includes(testResult)) return `${testResult} 이미지입니다. `;
        // if (testResult === "bright") return "이미지가 너무 밝습니다.";
        // if (testResult === "dark") return "이미지가 너무 어둡습니다.";
        return testResult === "success"
            ? "사용할 수 있는 이미지입니다."
            : "적절하지 않은 이미지입니다.";
    };

    const testResultP = () => {
        if (detectedObjects.includes(testResult))
            return "맞다면 확인 버튼을, 아니라면 우측 상단의 닫기 버튼을 눌러주세요.";
        // if (testResult === "bright")
        //     return "다른 이미지를 첨부해주세요.";
        // if (testResult === "dark")
        //     return "다른 이미지를 첨부해주세요.";
        return testResult === "success"
            ? "카드 생성을 계속 진행해주세요."
            : "다른 이미지를 첨부해주세요.";
    };

    return (
        <AiTestModalBg>
            <ModalAnimation isOpen={isModalOpen}>
                <AiTestModalBox className={isClosing ? "fade-out" : ""}>
                    <AiTestCloseButton onClick={handleClose}>
                        <img src={xButton} alt="Close" />
                    </AiTestCloseButton>
                    <h1>{testResultH1()} </h1>
                    <p>{testResultP()}</p>
                    <div>
                        <AiTestButton onClick={handleOk}>확인</AiTestButton>
                    </div>
                </AiTestModalBox>
            </ModalAnimation>
        </AiTestModalBg>
    );
};

export default AiTestModal;