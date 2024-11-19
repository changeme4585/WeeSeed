/*나이별 막대 그래프*/
import React from "react";
import styled, { keyframes } from "styled-components";

const ChartContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  width: 100%;
  height: 400px;
`;

const BarWrapper = styled.div`
  width: 100%;
  margin-top: 19px;
  display: flex;
  flex-direction: column;
  margin-bottom: 20px;
  position: relative;
`;

const BarContainer = styled.div`
  width: 100%;
  height: 8px;
  position: relative;
`;

const GrayBar = styled.div`
  position: absolute;
  width: 100%;
  height: 100%;
  background-color: #999999;
  border-radius: 4px;
`;

const grow = (percentage) => keyframes`
  from {
    width: 0%;
  }
  to {
    width: ${percentage}%;
  }
`;

const ColoredBar = styled.div`
  position: absolute;
  height: 100%;
  background-color: ${({ color }) => color};
  width: ${({ percentage }) => percentage}%;
  border-radius: 4px;
  animation: ${({ percentage }) => grow(percentage)} 1s ease-out forwards;
  transition: transform 0.2s ease-out;
  transform-origin: center;
  &:hover {
    transform: scaleY(1.5);
  }
`;

const Tooltip = styled.div`
  position: absolute;
  top: -25px;
  left: 50%;
  transform: translateX(-50%);
  background-color: #333;
  color: white;
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 12px;
  white-space: nowrap;
  display: none;
  ${BarContainer}:hover & {
    display: block;
  }
`;

const CategoryTitle = styled.p`
  margin-bottom: 12px;
  font-family: "Pretendard-Regular";
  font-size: 16px;
  color: #111111;
`;

const ValueLabel = styled.span`
  position: absolute;
  right: 0;
  top: -25px;
  font-family: "Pretendard-Regular";
  font-size: 14px;
  color: #111111;
`;

const AgeBarChart = ({ data = { aac: 0, video: 0 } }) => {
  const chartData = [
    {
      category: "말해봐요",
      value: data.aac || 0,
      color: "#A9CDE5",
    },
    {
      category: "따라해요",
      value: data.video || 0,
      color: "#B1C8FE",
    },
      /*
    {
      category: "이어봐요",
      value: 0,
      color: "#F4AFEF",
    },*/
  ];

  const totalCards = chartData.reduce((sum, d) => sum + d.value, 0);

  return (
    <ChartContainer>
      {chartData.map((item, index) => {
        const percentage =
          totalCards === 0 ? 0 : ((item.value / totalCards) * 100).toFixed(1);
        return (
          <BarWrapper key={index}>
            <CategoryTitle>{item.category}</CategoryTitle>
            <BarContainer>
              <GrayBar />
              <ColoredBar color={item.color} percentage={percentage} />
              <Tooltip>{item.value}개</Tooltip>
              <ValueLabel>
                {item.value}개({percentage}%)
              </ValueLabel>
            </BarContainer>
          </BarWrapper>
        );
      })}
    </ChartContainer>
  );
};

export default AgeBarChart;
