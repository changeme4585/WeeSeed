/*비디오 카드 생성틀*/
import React, { useState, useEffect, useRef } from "react";
import styled, { keyframes } from "styled-components";
import xButton from "../../images/svg/close-icon.svg";
import cardUploadIcon from "../../images/svg/add-icon-white.svg";
import SelectColor from "../SelectColor";
import axios from "axios";
import CardAnimation from "../AnimationComponent/CardAnimation"; // 애니메이션 주기

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

const CreateVideoModalBg = styled.div`
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

const CreateVideoModalBox = styled.div`
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

const CreateVideoLeftSection = styled.div`
  width: 480px;
  height: 870px;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  background-color: rgba(182, 174, 165, 1);
`;

const CreateVideoRightSection = styled.div`
  width: 390px;
  heigth: 870px;
  display: flex;
  position: relative;
`;

const CreateVideoVideoUploadButton = styled.div`
  width: 150px;
  height: 150px;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  img {
    width: 100%;
    height: 100%;
    object-fit: contain;
  }
`;

const CreateVideoVideoInput = styled.input`
  display: none;
`;

const CreateVideoCardNameInput = styled.input`
  font-family: "Pretendard-Regular";
  border: 1px solid #ffce1b !important;
  width: 304px;
  height: 48px;
  margin-top: 80px;
  margin-left: 40px;
  &.card-name-input {
    box-shadow: none;
    color: var(--black);
    font-size: 16px;
  }
  &.card-name-input:hover {
    box-shadow: none;
    background-color: rgba(255, 239, 133, 1);
    outline: none;
  }
  &.card-name-input:focus {
    box-shadow: none;
    outline: none;
    background-color: rgba(255, 253, 234, 1);
  }
  position: absolute;
  object-fit: contain;
`;

const CreateVideoTitle = styled.h2`
  font-family: "Pretendard-Regular";
  margin-top: 22px;
  margin-left: 110px;
  font-size: 31px;
  position: absolute;
  object-fit: contain;
`;

const CreateVideoButton = styled.button`
  background-color: rgba(255, 206, 27, 1);
  color: rgba(17, 17, 17, 1);
  cursor: pointer;
  font-size: 1rem;
  border: none;
  width: 310px;
  height: 40px;
  position: absolute;
  object-fit: contain;
  transition: background-color 0.3s ease;
  &:hover {
    background-color: rgba(255, 173, 0, 1);
    color: rgba(17, 17, 17, 1);
  }
  font-family: "Pretendard-Bold";
  margin-top: 790px;
  margin-left: 40px;
`;

const CreateVideoCloseButton = styled.button`
  position: absolute;
  background-color: transparent;
  border: none;
  cursor: pointer;
  margin-top: 20px;
  margin-left: 334px;
  margin-right: 20px;
  object-fit: contain;
`;

const CreateVideoThumbnail = styled.img`
  width: 100%;
  height: 100%;
  position: relative;
  object-fit: cover;
`;

const CreateVideoSelectColorContainer = styled.div`
  margin-left: 40px;
  margin-top: 156px;
  width: 310px;
  position: absolute;
  object-fit: contain;
`;

const CreateVideo = ({ closeModal, isModalOpen, fetchVideoCards }) => {
  const [video, setVideo] = useState(null);
  const [cardName, setCardName] = useState("");
  const [color, setColor] = useState("#767676");
  const [childCode, setChildCode] = useState("");
  const [constructorId, setConstructorId] = useState("");
  const [thumbnail, setThumbnail] = useState("");
  const canvasRef = useRef(null);
  const [isClosing, setIsClosing] = useState(false); // 닫힐때 애니메이션

  useEffect(() => {
    const selectedChildCode = localStorage.getItem("selectedChildCode");
    const constructorIdFromStorage = localStorage.getItem("userId");
    if (selectedChildCode) {
      setChildCode(selectedChildCode);
    }
    if (constructorIdFromStorage) {
      setConstructorId(constructorIdFromStorage);
    }
  }, []);

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    setVideo(file);
    const videoURL = URL.createObjectURL(file); // 비디오 URL 생성
    console.log("Video URL:", videoURL);
    setThumbnail("");
    extractThumbnail(videoURL);
  };

  const extractThumbnail = (videoURL) => {
    const videoElement = document.createElement("video");
    videoElement.src = videoURL;
    videoElement.onloadeddata = () => {
      videoElement.currentTime = 3;
      videoElement.onseeked = () => {
        const canvasElement = canvasRef.current;
        const context = canvasElement.getContext("2d");
        context.drawImage(
          videoElement,
          0,
          0,
          canvasElement.width,
          canvasElement.height
        );
        const dataURL = canvasElement.toDataURL("image/jpeg");
        console.log("Thumbnail Data URL:", dataURL);
        setThumbnail(dataURL);
        URL.revokeObjectURL(videoURL);
      };
    };
  };

  // 썸네일 이미지 데이터 파일로 만들기...
  const dataURLToFile = (dataURL, filename) => {
    const arr = dataURL.split(",");
    const mime = arr[0].match(/:(.*?);/)[1];
    const bstr = atob(arr[1]);
    let n = bstr.length;
    const u8arr = new Uint8Array(n);
    while (n--) {
      u8arr[n] = bstr.charCodeAt(n);
    }
    return new File([u8arr], filename, { type: mime });
  };

  const handleCreateVideo = async () => {
    if (!video || !cardName || !thumbnail) {
      alert("모든 정보를 입력해 주세요!");
      return;
    }
    const colorWithoutHash = color.replace("#", "");

    try {
      const formData = new FormData();
      formData.append("video", video);
      formData.append("cardName", cardName);
      formData.append("color", colorWithoutHash);
      formData.append("childCode", childCode);
      formData.append("constructorId", constructorId);
      //썸네일 같이 보내기...
      const thumbnailFile = dataURLToFile(
        thumbnail,
        `${cardName}_thumbnail.jpg`
      );
      console.log("Thumbnail File:", thumbnailFile);
      formData.append("thumbnail", thumbnailFile);

      await axios
        .post(`${process.env.REACT_APP_SERVER_IP}/video/upload`, formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        })
        .then((response) => {
          console.log("서버 응답:", response.data);
          fetchVideoCards();
          closeModal();
        })
        .catch((error) => {
          console.error(
            "서버 에러:",
            error.response ? error.response.data : error.message
          );
        });
    } catch (error) {
      console.error("카드 생성 실패:", error);
    }
  };
  const handleCloseButtonClick = () => {
    setIsClosing(true);
    setTimeout(() => {
      closeModal();
      setIsClosing(false);
    }, 200);
  };

  return (
    <CreateVideoModalBg>
      <CardAnimation isOpen={isModalOpen}>
        <CreateVideoModalBox className={isClosing ? "fade-out" : ""}>
          <CreateVideoLeftSection>
            {thumbnail ? (
              <CreateVideoThumbnail src={thumbnail} alt="비디오 썸네일" />
            ) : (
              <CreateVideoVideoUploadButton
                onClick={() => document.getElementById("videoUpload").click()}
              >
                <img src={cardUploadIcon} alt="Upload" />
              </CreateVideoVideoUploadButton>
            )}
            <CreateVideoVideoInput
              id="videoUpload"
              type="file"
              accept="video/*"
              onChange={handleFileChange}
            />
            <canvas
              ref={canvasRef}
              width="400"
              height="225"
              style={{ display: "none" }}
            />
          </CreateVideoLeftSection>
          <CreateVideoRightSection>
            <CreateVideoCloseButton onClick={handleCloseButtonClick}>
              <img src={xButton} alt="Close" />
            </CreateVideoCloseButton>
            <CreateVideoTitle>따라해요 추가</CreateVideoTitle>
            <CreateVideoCardNameInput
              type="text"
              placeholder="   카드명 입력"
              className="card-name-input"
              value={cardName}
              onChange={(e) => setCardName(e.target.value)}
            />
            <CreateVideoSelectColorContainer>
              <SelectColor setColor={setColor} small />
            </CreateVideoSelectColorContainer>
            <CreateVideoButton onClick={handleCreateVideo}>
              확인
            </CreateVideoButton>
          </CreateVideoRightSection>
        </CreateVideoModalBox>
      </CardAnimation>
    </CreateVideoModalBg>
  );
};

export default CreateVideo;
