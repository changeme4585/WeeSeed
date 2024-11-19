/*비디오 카드 틀*/
import React from "react";
import styled from "styled-components";
import PropTypes from "prop-types";

const CardContainer = styled.div`
  width: 100%;
  height: 100%;
  border: none;
  border-radius: 7px;
  display: flex;
  flex-direction: column;
  align-items: center;
  box-shadow: 0px 4px 2px rgba(0, 0, 0, 0.2);
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.1s;
  box-sizing: border-box;
  &:hover {
    transform: scale(1.02);
    box-shadow: 4px 7px 5px rgba(0, 0, 0, 0.1);
  }
`;

const CardName = styled.div`
  width: 100%;
  height: 40px;
  border-radius: 0 0 7px 7px;
  font-size: 16px;
  font-family: "Pretendard-Regular";
  text-align: left;
  box-sizing: border-box;
  padding-top: 11px;
  padding-left: 17px;
  color: #ffffff;
  background-color: ${({ $videoColor }) => $videoColor};
`;

const CardImage = styled.img`
  width: 100%;
  height: calc(100% - 40px); //이름 부분의 높이 제외
  object-fit: cover;
  border-radius: 7px 7px 0 0;
  box-sizing: border-box;
`;

const VideoCard = ({ videoName, videoColor, thumbNailSrc }) => {
  console.log("VideoCard props:", { videoName, videoColor, thumbNailSrc });
  return (
    <CardContainer>
      <CardImage src={thumbNailSrc} alt={videoName} />
      <CardName $videoColor={videoColor}>{videoName}</CardName>
    </CardContainer>
  );
};

VideoCard.propTypes = {
  videoName: PropTypes.string.isRequired,
  videoColor: PropTypes.string.isRequired,
  thumbNailSrc: PropTypes.string.isRequired,
};

export default VideoCard;
