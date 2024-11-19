/*유형별 막대 그래프*/
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

const TypeBarChart = ({ data }) => {
  console.log("장애유형별 막대 그래프 데이터:", data);
  if (!data) return null;
  //종 아동 수 계산
  const totalTypes = data.reduce((sum, d) => sum + d.value, 0);
  if (totalTypes === 0) {
    return <div>데이터가 없습니다.</div>;
  }

  return (
    <ChartContainer>
      {data.map((item, index) => {
        const percentage =
          totalTypes === 0 ? 0 : ((item.value / totalTypes) * 100).toFixed(1);
        return (
          <BarWrapper key={index}>
            <CategoryTitle>{item.category}</CategoryTitle>
            <BarContainer>
              <GrayBar />
              <ColoredBar color={item.color} percentage={percentage} />
              <Tooltip>{item.value}명</Tooltip>
              <ValueLabel>
                {item.value}명({percentage}%)
              </ValueLabel>
            </BarContainer>
          </BarWrapper>
        );
      })}
    </ChartContainer>
  );
};

export default TypeBarChart;
