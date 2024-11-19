import React, { useState, useContext, useEffect, useCallback } from "react";
import "../styles/Video.css";
import DeleteVideo from "../images/svg/delete-icon.svg";
import SelectColor from "../components/SelectColor";
import CreateVideo from "../components/videoComponent/CreateVideo";
import ViewVideo from "../components/videoComponent/ViewVideo"; // 비디오 보기창
import VideoList from "../components/videoComponent/VideoList"; // 비디오 리스트
import { ChildContext } from "../index";
import axios from "axios";
import addVideoIcon from "../images/svg/video-btn.svg"; // 따라해요 추가 버튼
import addSqIcon from "../images/svg/variety-video-btn.svg"; // 이어봐요 추가 버튼

function Video() {
  const { selectedChild, setSelectedChild } = useContext(ChildContext);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [videoCards, setVideoCards] = useState([]); // 비디오 카드 상태
  const [filteredVideoCards, setFilteredVideoCards] = useState([]); // 필터링된 비디오 카드 상태
  const [filteredColor, setFilteredColor] = useState(""); // 색깔 필터링
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [viewModalOpen, setViewModalOpen] = useState(false);
  const [selectedCard, setSelectedCard] = useState(null);

  const handleChildCount = useCallback(() => {
    const storedSelectedChild = JSON.parse(
        localStorage.getItem("selectedChild")
    );
    if (!storedSelectedChild) {
      console.log("아동과 연동하세요!");
      setSelectedChild(null);
    } else {
      setSelectedChild(storedSelectedChild);
    }
  }, [setSelectedChild]);

  useEffect(() => {
    handleChildCount();
  }, [handleChildCount]);

  const fetchVideoCards = useCallback(async () => {
    if (!selectedChild) {
      console.log("선택된 아동이 없음...");
      return;
    }

    setLoading(true);
    const constructorId = localStorage.getItem("userId");
    try {
      const response = await axios.get(
          `${process.env.REACT_APP_SERVER_IP}/video/get`,
          {
            params: {
              childCode: selectedChild.childCode,
              constructorId: constructorId,
            },
          }
      );

      if (response.status === 200) {
        console.log("Server response:", response.data);

        //파라미터 다 수정!!!
        const videoCardsData = response.data.map((card) => ({
          id: card.videoCardId, //videoCardId로 변경
          videoColor: `#${card.color}`,
          videoName: card.cardName,
          videoSrc: card.videoUrl, //videoUrl로 변경
          thumbNailSrc: card.thumbnailUrl, //thumbnailUrl로 변경
        }));
        console.log("받은 비디오 데이터:", videoCardsData);

        setVideoCards(videoCardsData);
        setFilteredVideoCards(videoCardsData); // 초기 필터링된 카드 설정
      } else {
        console.error("비디오 카드 목록 불러오기 실패");
        setError("비디오 카드 목록 불러오기 실패");
      }
    } catch (error) {
      console.error("비디오 카드 목록 불러오기 오류:", error);
      setError("비디오 카드 목록 불러오기 에러");
    } finally {
      setLoading(false);
    }
  }, [selectedChild]);

  useEffect(() => {
    if (selectedChild) {
      fetchVideoCards();
    }
  }, [selectedChild, fetchVideoCards]);

  //색깔 필터링
  useEffect(() => {
    if (filteredColor) {
      const filtered = videoCards.filter(
          (card) => card.videoColor === filteredColor
      );
      setFilteredVideoCards(filtered);
    } else {
      setFilteredVideoCards(videoCards);
    }
  }, [filteredColor, videoCards]);

  const openModal = () => {
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
  };

  const onVideocardClick = (card) => {
    setSelectedCard(card);
    setViewModalOpen(true);
  };
  const closeViewModal = () => {
    setViewModalOpen(false);
    setSelectedCard(null);
  };

  return (
      <>
        <div className="video-root">
          {/* 필터링창이 들어간 div */}
          <div className="video-top-div">
            <SelectColor setColor={setFilteredColor} />
          </div>
          <div className="video-content-btn">
            <div className="add-video-card-btn">
              <button className="add-video-btn" onClick={openModal}>
                <img
                    src={addVideoIcon}
                    alt="비디오 추가"
                    className="addVideoIcon"
                />
              </button>
              <button className="add-sequence-btn">
                <img src={addSqIcon} alt="시퀀스 추가" className="addSqIcon" />
              </button>
            </div>
            <div className="video-delete-btn">
              <img src={DeleteVideo} alt="비디오 삭제" className="DeleteVideo" />
            </div>
          </div>
          {loading ? (
              <p>Loading...</p>
          ) : error ? (
              <p>{error}</p>
          ) : (
              <VideoList
                  videoCards={filteredVideoCards}
                  onCardClick={onVideocardClick}
              />
          )}
        </div>
        {isModalOpen && (
            <CreateVideo
                isModalOpen={isModalOpen}
                closeModal={() => {
                  closeModal();
                  fetchVideoCards();
                }}
                fetchVideoCards={fetchVideoCards}
            /> //이부분 수정 잊지 말기..!!
        )}
        {viewModalOpen && (
            <ViewVideo
                card={selectedCard}
                isModalOpen={viewModalOpen}
                closeModal={closeViewModal}
                fetchVideoCards={fetchVideoCards}
            />
        )}
      </>
  );
}

export default Video;