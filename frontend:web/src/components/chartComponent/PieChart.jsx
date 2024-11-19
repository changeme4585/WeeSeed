/*원형그래프*/
/*그림 카드 수, 이미지 카드수, 비디오 카드수로 비율.. */
import React from "react";
import { ResponsivePie } from "@nivo/pie";
import styled from "styled-components";

const StyledPieChart = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;

  .custom-legend {
    position: absolute;
    right: 0px;
    display: flex;
    flex-direction: column;
    align-items: flex-start;

    .legend-item {
      display: flex;
      align-items: center;
      margin-bottom: 14px;

      .symbol {
        display: inline-block;
        width: 18px;
        height: 18px;
        border-radius: 50%;
        margin-right: 12px;
      }

      .label,
      .value {
        font-family: "Pretendard-Regular";
        font-size: 16px;
        color: #111111;
      }

      .label {
        flex: 1;
        min-width: 80px;
        text-align: left;
        margin-right: 10px;
      }

      .value {
        min-width: 100px;
        text-align: left;
      }
    }
  }
`;

const PieChart = ({ imageCardNum = 0, videoCardNum = 0 }) => {
  // 전체 카드수
  const totalCards = imageCardNum + videoCardNum;

  // 각 카드별 비율 계산
  const imageRatio = imageCardNum / totalCards;
  const videoRatio = videoCardNum / totalCards;
  const emptyRatio = 1 - imageRatio - videoRatio;

  // 파이차트 생성
  let data = [
    {
      id: "말해봐요",
      label: "말해봐요",
      value: imageRatio * 100,
      count: imageCardNum,
    },
    {
      id: "따라해요",
      label: "따라해요",
      value: videoRatio * 100,
      count: videoCardNum,
    },
    // { id: "이어봐요", label: "이어봐요", value: emptyRatio * 100, count: 0 },
  ];

  // 색 지정
  const colorMapping = {
    말해봐요: "#A9CDE5",
    따라해요: "#B1C8FE",
    // 이어봐요: "#F4AFEF",
    placeholder: "#999999",
  };

  //카드수 0일 경우 회색 차트로 ㅇㅇ
  if (totalCards === 0) {
    data = [{ id: "placeholder", label: "카드 없음", value: 100, count: 0 }];
  }

  //파이 차트 공백 없이 하기 위해...
  const filteredData = data.filter((d) => d.value > 0);
  return (
    <StyledPieChart style={{ height: 480 }}>
      <ResponsivePie
        data={filteredData}
        margin={{ top: 10, right: 260, bottom: 10, left: 0 }}
        innerRadius={0.5}
        padAngle={0}
        cornerRadius={3}
        activeOuterRadiusOffset={8}
        borderWidth={0}
        borderColor={{ from: "color", modifiers: [["darker", 0.2]] }}
        arcLinkLabelsSkipAngle={10}
        arcLinkLabelsTextColor="transparent"
        arcLinkLabelsThickness={2}
        arcLinkLabelsColor="transparent"
        arcLabelsSkipAngle={10}
        arcLabelsTextColor="transparent"
        animate={true}
        colors={({ id }) => colorMapping[id]}
        legends={[]}
      />
      <div className="custom-legend">
        {data.map((d) => (
          <div className="legend-item" key={d.id}>
            <div
              className="symbol"
              style={{ backgroundColor: colorMapping[d.id] }}
            ></div>
            <div className="label">{d.label}</div>
            <div className="value">
              {`${d.count}개 (${isNaN(d.value) ? 0 : d.value.toFixed(1)}%)`}
            </div>
          </div>
        ))}
      </div>
    </StyledPieChart>
  );
};

export default PieChart;
