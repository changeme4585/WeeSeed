import React, { useState, useCallback, useEffect } from "react";
import styled, { keyframes } from "styled-components";
import axios from "axios";
import xButton from "../../images/svg/close-icon.svg";
//모달창 애니메이션
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

const CreateGrowthModalBg = styled.div`
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

const CreateGrowthModalBox = styled.div`
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
    margin-top: 15px;
    margin-bottom: 20px;
    font-size: 16px;
  }
  div {
    display: flex;
    justify-content: space-between;
    width: 300px;
    height: 36px;
    margin-bottom: 30px;
  }
  &.fade-out {
    animation: ${ModalfadeOut} 0.1s ease-out forwards;
  }
`;

const CreateGrowthButton = styled.button`
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
  &:disabled {
    background-color: #d9d4cf;
    color: #767676;
    cursor: not-allowed;
  }
`;

const CreateGrowthButtonC = styled.button`
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

const CreateGrowthCloseButton = styled.button`
  position: absolute;
  top: 20px;
  right: 20px;
  background-color: transparent;
  border: none;
  cursor: pointer;
  font-size: 1.5rem;
`;

const CreateGrowthModal = ({ closeModal, onDiaryCreated, isModalOpen }) => {
  const [childName, setChildName] = useState("");
  const [isChildSelected, setIsChildSelected] = useState(false);
  const [selectedChild, setSelectedChild] = useState(null);
  const [isClosing, setIsClosing] = useState(false); // 닫힐때 애니메이션

  useEffect(() => {
    const storedChild = JSON.parse(localStorage.getItem("selectedChild"));
    if (storedChild && storedChild.name) {
      setChildName(storedChild.name);
      setIsChildSelected(true);
      setSelectedChild(storedChild);
    } else {
      setChildName(""); // 선택한 아동 없을경우 null로
      setIsChildSelected(false); //생성 못하게 막음
    }
  }, []);

  const handleConfirm = async () => {
    if (isChildSelected) {
      await createDiary(); // 일지 생성
      closeModal();
      onDiaryCreated();
    }
  };

  // 확인 버튼 누르면 post 요청
  const createDiary = useCallback(async () => {
    if (!selectedChild) return;
    const userId = localStorage.getItem("userId");

    try {
      const formData = new FormData();
      formData.append("userId", userId);
      formData.append("childCode", selectedChild.childCode);

      const response = await axios.post(
        `${process.env.REACT_APP_SERVER_IP}/growth/create`,
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        }
      );
      console.log("성장일지 생성 성공:", response.data);
    } catch (error) {
      if (error.response) {
        console.error("성장일지 생성 실패 - 응답 에러:", error.response.data);
      } else if (error.request) {
        console.error("성장일지 생성 실패 - 요청 에러:", error.request);
      } else {
        console.error("성장일지 생성 실패 - 설정 에러:", error.message);
      }
    }
  }, [selectedChild]);

  const handleCloseButtonClick = () => {
    setIsClosing(true);
    setTimeout(() => {
      closeModal();
      setIsClosing(false);
    }, 200);
  };
  return (
    <CreateGrowthModalBg>
      <ModalAnimation isOpen={isModalOpen}>
        <CreateGrowthModalBox className={isClosing ? "fade-out" : ""}>
          <CreateGrowthCloseButton onClick={handleCloseButtonClick}>
            <img src={xButton} alt="Close" />
          </CreateGrowthCloseButton>
          <h1>성장일지 생성</h1>
          <p>
            {childName
              ? `${childName}의 성장일지를 생성하시겠습니까?`
              : "성장일지를 생성하시겠습니까?"}
          </p>
          <div>
            <CreateGrowthButtonC onClick={handleCloseButtonClick}>
              취소
            </CreateGrowthButtonC>
            <CreateGrowthButton
              onClick={handleConfirm}
              disabled={!isChildSelected}
            >
              확인
            </CreateGrowthButton>
          </div>
        </CreateGrowthModalBox>
      </ModalAnimation>
    </CreateGrowthModalBg>
  );
};

export default CreateGrowthModal;
