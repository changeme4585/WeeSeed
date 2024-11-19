/*View AAC에서 수정 버튼 누르면 보여지는 창*/
//오디오, 카드색은 고정 ㅇㅇ..
import React, { useState, useEffect } from "react";
import styled from "styled-components";
import xButton from "../../images/svg/close-icon.svg";
import cardUploadIcon from "../../images/svg/add-icon-white.svg"; // 이미지 업로드 아이콘
import axios from "axios";

const ModifyAACModalBg = styled.div`
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

const ModifyAACModalBox = styled.div`
  display: flex;
  flex-direction: row;
  width: 1260px;
  height: 870px;
  background-color: var(--card-gray);
  border-radius: 10px;
  overflow: hidden;
  position: relative;
`;

const ModifyAACLeftSection = styled.div`
  width: 870px;
  display: flex;
  justify-content: flex-start;
  position: relative;
  gap: 20px;
`;

const ModifyAACRightSection = styled.div`
  width: 390px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  position: relative;
`;

const ModifyAACCloseButton = styled.button`
  position: absolute;
  top: 20px;
  right: 20px;
  background: none;
  border: none;
  cursor: pointer;
  object-fit: contain;
`;

const ModifyAACColorIcon = styled.div`
  width: 15px;
  height: 15px;
  background-color: ${({ color }) => color};
  border-radius: 50%;
  margin-right: 20px;
`;

const ModifyAACContainer = styled.div`
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
`;

const ModifyAACImg = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover;
`;

const ModifyAACImageUploadButton = styled.div`
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  background: none;
  opacity: 0.7;

  img {
    width: 37.24%;
    height: 37.24%;
    object-fit: cover;
  }
`;

const ModifyAACImageInput = styled.input`
  display: none;
`;

const ModifyAACInput = styled.input`
  margin-top: 120px;
  margin-left: 40px;
  width: 287px;
  border: 1px solid rgba(255, 173, 0, 1);
  font-family: "Pretendard-Bold";
  && {
    font-size: 16px;
  }
  height: 48px;
  padding-top: 5px;
  padding-left: 20px;
  background-color: #fffdea;

  &&::placeholder {
    font-size: 16px;
  }
  &:focus {
    outline: none;
    border-color: rgba(255, 173, 0, 1);
    background-color: rgba(255, 239, 133, 1);
  }
  &:hover {
    background-color: rgba(255, 239, 133, 1);
  }
`;

const ModifyAACColorLabel = styled.div`
  display: flex;
  font-family: "Pretendard-Regular";
  font-size: 17px;
  margin-bottom: 570px;
  margin-left: 45px;
  margin-top: 32px;
`;
const ModifyAACTitle = styled.h2`
  font-family: "Pretendard-Regular";
  top: 2.53%;
  font-size: 2rem;
  position: absolute;
  margin-left: 100px;
  margin-top: 2px;
`;

const ModifyCardbutton = styled.button`
  background-color: rgba(255, 206, 27, 1);
  color: rgba(17, 17, 17, 1);
  cursor: pointer;
  font-size: 1rem;
  border: none;
  width: 79.49%;
  height: 4.6%;
  position: absolute;
  transition: background-color 0.3s ease;
  &:hover {
    background-color: rgba(255, 173, 0, 1);
    color: rgba(17, 17, 17, 1);
  }
  font-family: "Pretendard-Bold";
  bottom: 4.6%;
  margin-left: 40px;
`;

const ModifyAAC = ({ card, closeModifyModal, fetchAACCards }) => {
  const [newCardName, setNewCardName] = useState(card.imgName); // 수정 전 초기 카드명
  const [newImage, setNewImage] = useState(null); // 수정 이미지

  useEffect(() => {
    console.log("ModifyAAC is rendering, card:", card);
  }, [card]);

  // 이미지 변경 처리
  const handleImageChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setNewImage(file);
    }
  };

  // 서버에 전송
  const handleFormSubmit = async () => {
    console.log("카드 수정 후 서버에 보내기 전 aac 카드 데이터:", card);
    const constructorId = localStorage.getItem("userId");
    const childCode = localStorage.getItem("selectedChildCode");
    const formData = new FormData();

    // 이미지 변경 여부 확인
    const isImageChanged = newImage !== null;

    // 이미지가 변경되지 않은 경우 경고 메시지 표시
    if (!isImageChanged) {
      alert("이미지를 변경하세요.");
      return; // 이미지가 변경되지 않으면 함수를 종료합니다.
    }

    let imageBlob;

    if (isImageChanged) {
      imageBlob = newImage;
    } else {
      // 기존 이미지 사용
      if (card.imgSrc) {
        try {
          if (card.imgSrc.startsWith("data:image")) {
            imageBlob = await fetch(card.imgSrc).then((res) => res.blob());
            console.log("기존 이미지 blob으로 변경:", imageBlob);
          } else if (card.imgSrc.startsWith("http://") || card.imgSrc.startsWith("https://")) {
            imageBlob = await fetch(card.imgSrc).then((res) => res.blob());
            console.log("기존 이미지 url로 변경:", imageBlob);
          } else {
            console.error("card.imgSrc 이미지 포맷 실패:", card.imgSrc);
          }
        } catch (error) {
          console.error("이미지 fetch 오류:", error);
        }
      } else {
        console.error("card.imgSrc가 정의되지 않았습니다.");
      }
    }

// Blob을 File로 변환 (이미지 변경 없을 경우 처리)
    if (imageBlob && !(imageBlob instanceof File)) {
      imageBlob = new File([imageBlob], "existImage.png", {
        type: "image/png",
      });
    }


    formData.append("image", imageBlob, imageBlob.name);
    formData.append("childCode", card.childCode || childCode);
    formData.append("constructorId", card.constructorId || constructorId);
    formData.append("aacCardId", card.id); //다시 이부분 수정..
    formData.append("cardName", card.imgName); // 기존 이름

    //이름 바뀌는지 안 바뀌는지 체크
    const isNameChange = newCardName !== card.imgName;
    //이름 안 바뀌어도 바뀐 이름으로 보내지게 처리
    formData.append("newCardName", isNameChange ? newCardName : card.imgName);

    // 전송하는 데이터 확인을 위한 콘솔 로그..
    for (let pair of formData.entries()) {
      console.log(pair[0], pair[1]);
    }

    try {
      await axios.post(
          `${process.env.REACT_APP_SERVER_IP}/aac/update`,
          formData,
          {
            headers: { "Content-Type": "multipart/form-data" },
          }
      );
      console.log("카드 업데이트 성공!");
      fetchAACCards();
    } catch (error) {
      console.error(
          "카드 업데이트 오류:",
          error.response ? error.response.data : error
      );
    }
    closeModifyModal(); //변한거 없으면 걍 창 닫히는걸로 처리함
  };

  return (
      <ModifyAACModalBg>
        <ModifyAACModalBox>
          <ModifyAACLeftSection>
            <ModifyAACContainer>
              <ModifyAACImg
                  src={newImage ? URL.createObjectURL(newImage) : card.imgSrc}
                  alt={card.imgName}
              />
              <ModifyAACImageUploadButton
                  onClick={() =>
                      document.getElementById("modifyImageUpload").click()
                  }
              >
                <img src={cardUploadIcon} alt="Upload" />
              </ModifyAACImageUploadButton>
              <ModifyAACImageInput
                  id="modifyImageUpload"
                  type="file"
                  accept="image/*"
                  onChange={handleImageChange}
              />
            </ModifyAACContainer>
          </ModifyAACLeftSection>

          <ModifyAACRightSection>
            <ModifyAACCloseButton onClick={closeModifyModal}>
              <img src={xButton} alt="Close" />
            </ModifyAACCloseButton>
            <ModifyAACTitle>말해봐요 수정</ModifyAACTitle>
            <ModifyAACInput
                type="text"
                value={newCardName}
                onChange={(e) => setNewCardName(e.target.value)}
                placeholder="  "
            />
            <ModifyAACColorLabel>
              <ModifyAACColorIcon color={card.imgColor} />
              라벨링
            </ModifyAACColorLabel>
            <ModifyCardbutton onClick={handleFormSubmit}>
              카드 수정
            </ModifyCardbutton>
          </ModifyAACRightSection>
        </ModifyAACModalBox>
      </ModifyAACModalBg>
  );
};

export default ModifyAAC;