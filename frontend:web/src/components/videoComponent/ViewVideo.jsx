/*비디오 카드 클릭시 보여지는 창*/
import React, { useState, useEffect } from "react";
import styled, { keyframes } from "styled-components";
import xButton from "../../images/svg/close-icon.svg";
import modifyBtn from "../../images/svg/modify-icon.svg";
import DeleteImg from "../../images/svg/delete-icon.svg";
import CardAnimation from "../AnimationComponent/CardAnimation"; // 애니메이션 주기
import DeleteModal from "../userComponent/DeleteModal";

//비디오 재생을 위한 라이브러리 import
import ReactPlayer from "react-player";

//창 닫힐때 애니메이션
const fadeOut = keyframes`
  from {
    opacity: 1;
    transform: translateY(0px);
  }
  to {
    opacity: 0;
    transform: translateY(20px);
  }
`;

const ViewVideoModalBg = styled.div`
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

const ViewVideoModalBox = styled.div`
  display: flex;
  flex-direction: row;
  width: 870px;
  height: 870px;
  background-color: rgba(247, 246, 245, 1);
  border-radius: 10px;
  overflow: hidden;
  position: relative;
  &.fade-out {
    animation: ${fadeOut} 0.4s ease-out forwards;
  }
`;

const ViewVideoRightSection = styled.div`
  width: 390px;
  height: 870px;
  display: flex;
  position: relative;
`;

const ViewVideoCloseButton = styled.button`
  position: absolute;
  background-color: transparent;
  border: none;
  cursor: pointer;
  margin-top: 20px;
  margin-left: 334px;
  margin-right: 20px;
  object-fit: contain;
`;

const ViewVideoModifyButton = styled.button`
  position: absolute;
  background-color: transparent;
  border: none;
  cursor: pointer;
  margin-top: 20px;
  margin-left: 20px;
  object-fit: contain;
`;

const ViewVideoTitle = styled.h2`
  font-family: "Pretendard-Regular";
  margin-top: 76px;
  margin-left: 40px;
  font-size: 42px;
  position: absolute;
  object-fit: contain;
`;

const ViewVideoDeleteButton = styled.button`
  background-color: transparent;
  cursor: pointer;
  position: absolute;
  border: none;
  object-fit: contain;
  margin-top: 814px;
  margin-left: 334px;
`;

const ViewVideoColorLabel = styled.div`
  display: flex;
  font-family: "Pretendard-Regular";
  margin-top: 150px;
  margin-left: 45px;
  position: absolute;
  object-fit: contain;
  font-size: 17px;
`;

const ViewVideoColorIcon = styled.div`
  width: 15px;
  height: 15px;
  background-color: ${({ color }) => color};
  border-radius: 50%;
  margin-right: 20px;
`;

/*왼쪽 구역*/
const ViewVideoLeftSection = styled.div`
  width: 480px;
  height: 870px;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  overflow: hidden;
`;

const ViewVideoContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  position: relative;
  overflow: hidden;
`;

const StyledReactPlayer = styled(ReactPlayer)`
  width: 100% !important;
  height: 100% !important;
  object-fit: cover;
`;

const ViewVideo = ({ card, closeModal, isModalOpen, fetchVideoCards }) => {
  const [isEditing, setIsEditing] = useState(false);
  const [editedCardName, setEditedCardName] = useState(card.videoName);
  const [isClosing, setIsClosing] = useState(false); // 닫힐때 애니메이션
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false); // 카드 삭제 모달

  //삭제 처리
  const openDeleteModal = () => {
    setIsDeleteModalOpen(true);
  };

  const closeDeleteModal = () => {
    setIsDeleteModalOpen(false);
  };

  const handleEditToggle = () => {
    //setIsEditing(!isEditing);
  };

  const handleCardNameChange = (e) => {
    setEditedCardName(e.target.value);
  };

  const handleSave = () => {
    setIsEditing(false);
  };
  const handleCloseButtonClick = () => {
    setIsClosing(true);
    setTimeout(() => {
      closeModal();
      setIsClosing(false);
    }, 200);
  };
  useEffect(() => {
    console.log("Received card:", card);
  }, [card]);

  return (
    <ViewVideoModalBg>
      <CardAnimation isOpen={isModalOpen}>
        <ViewVideoModalBox className={isClosing ? "fade-out" : ""}>
          <ViewVideoLeftSection>
            <ViewVideoContainer>
              <StyledReactPlayer
                url={card.videoSrc}
                controls
                playing //자동재생 여부
              />
            </ViewVideoContainer>
          </ViewVideoLeftSection>
          <ViewVideoRightSection>
            <div style={{ width: "100%" }}>
              <ViewVideoModifyButton onClick={handleEditToggle}>
                <img src={modifyBtn} alt="Modify" />
              </ViewVideoModifyButton>
              <ViewVideoCloseButton onClick={handleCloseButtonClick}>
                <img src={xButton} alt="Close" />
              </ViewVideoCloseButton>
              <ViewVideoTitle>
                {isEditing ? "영상 수정" : card.videoName}
              </ViewVideoTitle>
              {isEditing ? (
                <input
                  type="text"
                  value={editedCardName}
                  onChange={handleCardNameChange}
                  style={{ width: "100%", fontSize: "1rem" }}
                />
              ) : null}
              <ViewVideoColorLabel>
                <ViewVideoColorIcon color={card.videoColor} />
                라벨링
              </ViewVideoColorLabel>
            </div>
            {isEditing ? (
              <button
                onClick={handleSave}
                style={{ width: "100%", fontSize: "1rem" }}
              >
                Save
              </button>
            ) : null}
            <ViewVideoDeleteButton onClick={openDeleteModal}>
              <img src={DeleteImg} alt="delete" />
            </ViewVideoDeleteButton>
          </ViewVideoRightSection>
        </ViewVideoModalBox>
      </CardAnimation>
      {isDeleteModalOpen && (
        <DeleteModal
          isModalOpen={isDeleteModalOpen}
          closeModal={() => {
            //얘도 닫히게
            closeDeleteModal();
            handleCloseButtonClick();
          }}
          card={card}
          fetchVideoCards={fetchVideoCards} //삭제후 목록 리로드
        />
      )}
    </ViewVideoModalBg>
  );
};

export default ViewVideo;
