/*성별 원형 그래프 차트*/
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

const GenderPieChart = ({ data = {}, genderGroup }) => {
  console.log("성별 원형 그래프 데이터:", data);

  //성별에 따라 파이 그래프 데이터 분류
  const totalCards = data.aac + data.video;
  const aacRatio = totalCards === 0 ? 0 : (data.aac / totalCards) * 100;
  const videoRatio = totalCards === 0 ? 0 : (data.video / totalCards) * 100;
  const emptyRatio = Math.max(0, 100 - aacRatio - videoRatio);

  //파이 그래프 데이터
  let chartData = [
    { id: "말해봐요", label: "말해봐요", value: aacRatio, count: data.aac },
    { id: "따라해요", label: "따라해요", value: videoRatio, count: data.video },
    //{ id: "이어봐요", label: "이어봐요", value: emptyRatio, count: 0 },
  ];

  // 색 지정
  const colorMapping = {
    말해봐요: "#A9CDE5",
    따라해요: "#B1C8FE",
    // 이어봐요: "#F4AFEF",
    placeholder: "#999999",
  };

  if (totalCards === 0) {
    chartData = [
      { id: "placeholder", label: "카드 없음", value: 100, count: 0 },
    ];
  }
  // 공백 데이터 제외
  const filteredData = chartData.filter((d) => d.value > 0);

  return (
    <StyledPieChart style={{ height: 480 }}>
      <ResponsivePie
        data={filteredData}
        margin={{ top: 10, right: 260, bottom: 10, left: 0 }}
        innerRadius={0.5}
        padAngle={0.7}
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
        {chartData.map((d) => (
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

export default GenderPieChart;
