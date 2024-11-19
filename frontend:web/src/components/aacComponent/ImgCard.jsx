/*AAC 카드 프레임(틀)*/
import React, {useState} from "react";
import styled from "styled-components";
import PropTypes from "prop-types";

const CardContainer = styled.div`
  width: 220px;
  height: 264px;
  border: none;
  outline: none;
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
  padding-top: 12px;
  padding-left: 17px;
  color: #ffffff;
  background-color: ${({ $imgColor }) => $imgColor};
`;

const CardImage = styled.img`
  width: 100%;
  height: calc(100% - 40px);
  object-fit: cover;
  border-radius: 7px 7px 0 0;
  box-sizing: border-box;
`;

// 카드 이름 변환 함수
export const translateCardName = (name) => {
  // 카드 이름 매핑 객체
  const cardNameMapping = {
    dad: "아빠",
    mom: "엄마",
    giveme: "주세요",
    hello: "안녕하세요",
    rice: "밥",
    no: "아니요",
    yes: "네",
    sick: "아파요",
    teacher: "선생님",
    sleep: "잠",
    toilet: "화장실",
  };
  return cardNameMapping[name.toLowerCase()] || name;
};

const ImgCard = ({ imgName, imgColor, imgSrc, audioSrc, isDefault }) => {
  console.log("aac 카드 props:", { imgName, imgColor, imgSrc, audioSrc });

  const translatedName = translateCardName(imgName);
  const [source, setSource] = useState(imgSrc); // 상태를 관리하여 초기 이미지 설정

  const handleImageError = (e) => {
    setSource("src/images/svg/error-icon.svg"); // 이미지 오류 발생 시 기본 이미지로 변경
  };

  return (
      <CardContainer>
        <CardImage src={source} alt={translatedName} onError={handleImageError} />
        <CardName $imgColor={imgColor}>{translatedName}</CardName>
      </CardContainer>
  );
};


ImgCard.propTypes = {
  imgName: PropTypes.string.isRequired,
  imgColor: PropTypes.string.isRequired,
  imgSrc: PropTypes.string.isRequired,
  audioSrc: PropTypes.string,
  isDefault: PropTypes.bool,
  id: PropTypes.string,
};

export default React.memo(ImgCard);
