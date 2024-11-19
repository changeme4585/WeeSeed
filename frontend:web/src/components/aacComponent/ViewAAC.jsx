/*AAC 카드 클릭시 보여지는 창*/
import React, { useState, useEffect, useRef } from "react";
import axios from "axios";
import styled, { keyframes } from "styled-components";
import xButton from "../../images/svg/close-icon.svg";
import modifyBtn from "../../images/svg/modify-icon.svg";
import PrintImg from "../../images/svg/print-icon.svg";
import DeleteImg from "../../images/svg/delete-icon.svg";
import RightBtn from "../../images/svg/aac-list-btn-right.svg"; //오른쪽 버튼
import LeftBtn from "../../images/svg/aac-list-btn-left.svg"; //왼쪽 버튼
import CardAnimation from "../AnimationComponent/CardAnimation"; // 애니메이션 주기
import ModifyAAC from "./ModifyAAC";
import DeleteModal from "../userComponent/DeleteModal";
import { translateCardName } from "./ImgCard"; // 디폴트 카드 이름 한글로 변환
//오디오 재생바
import AudioPlayer from "./AudioPlayer";
//프린트용
import ReactToPrint from "react-to-print"; //개별 프린트용

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

//이미지 확장시 슬라이드 애니메이션
//오른쪽으로 넘어감
const slideRight = keyframes`
  from {
    transform: translateX(100%);
  }
  to {
    transform: translateX(0);
  }
`;
// //왼쪽으로 넘어감
// const slideLeft = keyframes`
//   from {
//     transform: translateX(0);
//   }
//   to {
//     transform: translateX(-100%);
//   }
// `;

const ViewAACModalBg = styled.div`
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

const ViewAACModalBox = styled.div`
  display: flex;
  flex-direction: row;
  width: 1260px;
  height: 870px;
  background-color: rgba(247, 246, 245, 1);
  border-radius: 10px;
  overflow: hidden;
  position: relative;

  &.fade-out {
    animation: ${fadeOut} 0.4s ease-out forwards;
  }
`;

const ViewAACLeftSection = styled.div`
  width: 870px;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
`;

const ViewAACRightSection = styled.div`
  width: 390px;
  display: flex;
  position: relative;
`;

const ViewAACCloseButton = styled.button`
  position: absolute;
  background-color: transparent;
  border: none;
  cursor: pointer;
  margin-top: 20px;
  margin-left: 334px;
  margin-right: 20px;
  object-fit: contain;
`;

const ViewAACModifyButton = styled.button`
  position: absolute;
  background-color: transparent;
  border: none;
  cursor: pointer;
  margin-top: 20px;
  margin-left: 20px;
  object-fit: contain;
`;

const ViewAACTitle = styled.h2`
  font-family: "Pretendard-Regular";
  margin-top: 76px;
  margin-left: 40px;
  font-size: 42px;
  position: absolute;
  object-fit: contain;
`;

const ViewAACPlayButton = styled.button`
  background-color: transparent;
  color: var(--black);
  font-size: 1rem;
  border: none;
  display: flex;
  flex-direction: column;
  align-items: start;
  font-family: "Pretendard-Regular";
  justify-content: space-between;
  position: absolute;
  object-fit: contain;
  margin-top: 222px;
  margin-left: 40px;

  p {
    margin-bottom: 36px;
  }
`;

const ViewAACPrintButton = styled.button`
  background-color: transparent;
  cursor: pointer;
  border: none;
  position: absolute;
  font-family: "Pretendard-Regular";
  object-fit: contain;
  margin-top: 814px;
  margin-left: 20px;
`;

const ViewAACDeleteButton = styled.button`
  background-color: transparent;
  cursor: pointer;
  position: absolute;
  border: none;
  object-fit: contain;
  margin-top: 814px;
  margin-left: 334px;
`;

const ViewAACColorLabel = styled.div`
  display: flex;
  font-family: "Pretendard-Regular";
  margin-top: 150px;
  margin-left: 45px;
  position: absolute;
  object-fit: contain;
  font-size: 17px;
`;

const ViewAACColorIcon = styled.div`
  width: 15px;
  height: 15px;
  background-color: ${({ color }) => color};
  border-radius: 50%;
  margin-right: 20px;
`;

/*왼쪽구역*/
const ViewAACContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  position: relative;
`;
//aac 카드가 여러장일 경우 버튼으로 넘겨 볼수 있게!!
const CardListButton = styled.img`
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  cursor: pointer;
  z-index: 1;
  width: 44px;
  height: 44px;
`;
// //왼쪽 버튼
// const LeftCardButton = styled(CardListButton)`
//   left: 10px;
// `;
//오른쪽 버튼
const RightCardButton = styled(CardListButton)`
  right: 10px;
`;

//원래는 object-fit이 없었음!!
const ViewAACImg = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover;
  animation: ${(props) =>
      props.slideDirection === "slideRight" ? slideRight : "none"}
      //props.slideDirection === "slideRight"
      //    ? slideRight
      //    : props.slideDirection === "slideLeft"
      //        ? slideLeft
      //        : "none"}
  0.5s forwards;

  @media print {
    width: 100%;
    height: 100%;
  }
`;

const PrintBox = styled.div`
  display: none;
  @media print {
    display: flex;
    flex-direction: column;
    align-items: center;
    border-radius: 15px;
    background-color: ${({ bgColor }) => bgColor || "#767676"};
    width: 220px;
    height: 264px;
    text-align: center;
    overflow: hidden;
  }
`;

const PrintTitle = styled.h2`
  @media print {
    font-family: "Pretendard-Regular";
    font-size: 15px;
    color: white;
  }
`;

const ViewAAC = ({ card, closeModal, isModalOpen, fetchAACCards }) => {
  const [isModifyOpen, setIsModifyOpen] = useState(false);
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false); // 카드 삭제 모달
  const translatedName = translateCardName(card.imgName); //디폴트 카드 이름 변환
  const [isClosing, setIsClosing] = useState(false); //닫힐때 애니메이션
  const [extendedCards, setExtendedCards] = useState([]); //발음 테스트 후 추가된 카드 리스트 저장
  const [currentCardIndex, setCurrentCardIndex] = useState(0); //사진 전환을 위한 인덱스 저장!
  const [slideDirection, setSlideDirection] = useState(null); //슬라이드 애니메이션용.. 방향 저장
  const componentRef = useRef(); //프린틍용 참조

  //오류 확인용 콘솔 로그..
  useEffect(() => {
    console.log("현재 카드 index :", currentCardIndex);
    console.log("slide된 방향:", slideDirection);
  }, [currentCardIndex, slideDirection]);

  useEffect(() => {
    if (isModalOpen) {
      setSlideDirection("none"); //처음 열렸을때 슬라이드 방지;;
      setCurrentCardIndex(0); // 첫번째 이미지로
      fetchExtendedCards(); //ViewAAC 열ㄹ렸을때 axios 요청 함수...
    }
  }, [isModalOpen]);

  useEffect(() => {
    console.log("Received card:", card);
  }, [card]);

  //확장된 aac 카드 리스트(=발음 테스트 통과 후 추가되는 이미지들) 불러오기!!
  const fetchExtendedCards = async () => {
    //디폴트 카드는 제외
    if (card.type === "default") {
      setExtendedCards([]); //빈 배열로 설정
      return;
    }
    const repCardId = card.id;
    const requestUrl = `${process.env.REACT_APP_SERVER_IP}/extend-aac/get/${repCardId}`;
    console.log("선택된 카드 id:", repCardId);
    try {
      const response = await axios.get(
          requestUrl, {
            headers: { 'Cache-Control': 'no-cache'},
          }
      );
      //불러오기 성공
      if (response.status === 200) {
        // response data의 imageUrl에 AWS S3 BASE URL 추가
        const s3BaseUrl = process.env.REACT_APP_S3_BASE_URL; // AWS S3 BASE URL을 환경 변수에서 가져옴
        setExtendedCards(
            response.data.map((img) => ({
              ...img,
              imageUrl: `${s3BaseUrl}${img.imageUrl}`, // S3 BASE URL 추가
            }))
        );
        console.log("불러온 extend 카드 리스트:", response.data); //내용 출력
      }
    } catch (error) {
      if (error.response.status === 404) {
        setExtendedCards([]); //확장 카드 빈 배열로 처리
        console.log("확장 카드 없음");
      } else if (error.response.status === 500) {
        console.log("서버 오류 발생");
      }
      console.error("extended card 불러오기 오류:", error);
    }
  };

  // //버튼 누르면 사진 전환되게
  // //왼쪽 버튼 누르면 이전 사진
  // const handlePreCard = () => {
  //   setSlideDirection("slideLeft");
  //   setCurrentCardIndex((preIndex) =>
  //       preIndex > 0 ? preIndex - 1 : extendedCards.length
  //   ); //수정
  // };

  //오른쪽 버튼 누르면 다음 사진으로
  //리스트 끝까지 가서 오른쪽 누르면 첫 사진으로 돌아감->공부용으론 루프가 나을거 같아서 이대로 유지
  const handleNextCard = () => {
    setSlideDirection("none");
    setTimeout(() => {
      setSlideDirection("slideRight");
      setCurrentCardIndex((preIndex) =>
          preIndex < extendedCards.length ? preIndex + 1 : 0
      );
    }, 0);
  };

  const currentImgSrc =
      currentCardIndex === 0 //원래 카드는 현재 index 0이게 설정해두기
          ? card.imgSrc
          : extendedCards[currentCardIndex - 1]?.imageUrl;

  //삭제 처리
  const openDeleteModal = () => {
    setIsDeleteModalOpen(true);
  };

  const closeDeleteModal = () => {
    setIsDeleteModalOpen(false);
  };

  const handleModifyClick = () => {
    setIsModifyOpen(true);
  };

  const handleCloseButtonClick = () => {
    setIsClosing(true);
    setTimeout(() => {
      closeModal();
      setIsClosing(false);
    }, 100);
  };

  return (
      <>
        {!isModifyOpen && (
            <ViewAACModalBg>
              <CardAnimation isOpen={isModalOpen}>
                <ViewAACModalBox>
                  <ViewAACLeftSection>
                    <ViewAACContainer>
                      {extendedCards.length > 0 && ( //추가된 유사 이미지 리스트가 있을경우에만!! 넘기기 버튼 보이게 ^-^
                          <>
                            {/*{currentCardIndex > 0 && ( //맨 처음 카드땐 왼쪽 버튼 안보이게ㅇㅇ*/}
                            {/*    <LeftCardButton*/}
                            {/*        alt="Left"*/}
                            {/*        src={LeftBtn}*/}
                            {/*        onClick={handlePreCard}*/}
                            {/*    />*/}
                            {/*)}*/}
                            <RightCardButton
                                alt="Right"
                                src={RightBtn}
                                onClick={handleNextCard}
                            />
                          </>
                      )}
                      <ViewAACImg
                          src={currentImgSrc}
                          alt={translatedName}
                          slideDirection={slideDirection}
                          ref={componentRef}
                      />
                    </ViewAACContainer>
                  </ViewAACLeftSection>

                  <ViewAACRightSection>
                    {/* card의 타입이 default가 아닐 경우만 수정 버튼 렌더링 */}
                    {card.type !== "default" && (
                        <ViewAACModifyButton onClick={handleModifyClick}>
                          <img src={modifyBtn} alt="Modify" />
                        </ViewAACModifyButton>
                    )}
                    <ViewAACCloseButton onClick={handleCloseButtonClick}>
                      <img src={xButton} alt="Close" />
                    </ViewAACCloseButton>
                    <ViewAACTitle>{translatedName}</ViewAACTitle>
                    <ViewAACColorLabel>
                      <ViewAACColorIcon color={card.imgColor} />
                      라벨링
                    </ViewAACColorLabel>
                    <ViewAACPlayButton>
                      <p>음성 재생하기</p>
                      <AudioPlayer audioSrc={card.audioSrc} />
                    </ViewAACPlayButton>
                    <ReactToPrint /* 중요: 프린트창->옵션-> 배경 그래픽 체크 필수 */
                        trigger={() => (
                            <ViewAACPrintButton>
                              <img src={PrintImg} alt="Print" />
                            </ViewAACPrintButton>
                        )}
                        content={() => componentRef.current} //선택한 이미지 프린트
                        pageStyle={`@page { size: A4; margin: 20mm; }`} // 페이지 a4용지로 설정
                    />
                    <PrintBox ref={componentRef} bgColor={card.imgColor}>
                      <ViewAACImg src={currentImgSrc} alt={translatedName} />
                      <PrintTitle style={{ fontSize: "15px" }}>
                        {translatedName}
                      </PrintTitle>                    </PrintBox>
                    <ViewAACDeleteButton onClick={openDeleteModal}>
                      <img src={DeleteImg} alt="delete" />
                    </ViewAACDeleteButton>
                  </ViewAACRightSection>
                </ViewAACModalBox>
              </CardAnimation>
            </ViewAACModalBg>
        )}
        {isModifyOpen && (
            <ModifyAAC
                card={card}
                closeModifyModal={() => {
                  setIsModifyOpen(false);
                  closeModal();
                }}
                fetchAACCards={fetchAACCards}
            />
        )}
        {/*삭제 모달 처리*/}
        {isDeleteModalOpen && (
            <DeleteModal
                isModalOpen={isDeleteModalOpen}
                closeModal={() => {
                  //얘도 닫히게
                  closeDeleteModal();
                  handleCloseButtonClick();
                }}
                card={card}
                fetchAACCards={fetchAACCards} // 삭제 후 목록 새로고침
            />
        )}
      </>
  );
};

export default ViewAAC;