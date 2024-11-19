/*비디오 리스트 div 컴포넌트*/
import React from "react";
import styled from "styled-components";
import VideoCard from "./VideoCard"; // 비디오 카드 틀
import PropTypes from "prop-types";
import NotFoundImg from "../../images/svg/error-icon.svg"; //404 그림

const VideoCardContainer = styled.div`
  display: flex;
  flex-wrap: wrap;
  gap: 40px;
  width: 100%;
  max-width: 1530px;
  margin: 0px;
  overflow: hidden;
`;

const VideoCardWrapper = styled.div`
  width: 220px;
  height: 434px;
  box-sizing: border-box;
`;

const VideoNotFound = styled.img`
  margin-left: 591px;
  margin-top: 140px;
`;
const VideoList = ({ videoCards, onCardClick }) => {
  return (
    <VideoCardContainer>
      {videoCards.length === 0 ? (
        <VideoNotFound src={NotFoundImg} alt="video not found"></VideoNotFound>
      ) : (
        videoCards.map((card) => (
          <VideoCardWrapper
            key={card.id}
            onClick={() => onCardClick({ ...card, type: "video" })}
          >
            <VideoCard
              videoName={card.videoName}
              videoColor={card.videoColor}
              thumbNailSrc={card.thumbNailSrc}
              childId={card.childId}
            />
          </VideoCardWrapper>
        ))
      )}
    </VideoCardContainer>
  );
};

VideoList.propTypes = {
  videoCards: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired,
      videoName: PropTypes.string.isRequired,
      videoColor: PropTypes.string.isRequired,
      thumbNailSrc: PropTypes.string.isRequired,
      childId: PropTypes.string.isRequired,
    })
  ).isRequired,
  onCardClick: PropTypes.func.isRequired,
};

export default VideoList;
