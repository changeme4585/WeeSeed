import tensorflow as tf
from tensorflow.keras.applications import MobileNetV2
from tensorflow.keras import layers, models
import glob
import os

# 저장된 모델 경로 설정
model_save_path = "/Users/Heily/Desktop/WeeSeed_Visual/pythonProject/pythonProject1/saved_models/my_fashion_model_train_model4"

# 모델 로드
model = tf.keras.models.load_model(model_save_path)

# 데이터셋 로드 함수 (이전과 동일)
def parse_fashion_tfrecord(tfrecord):
    feature_description = {
        "image": tf.io.FixedLenFeature([], tf.string),
        "image_name": tf.io.FixedLenFeature([], tf.string),
        "base_colour": tf.io.FixedLenFeature([], tf.int64),
        "target": tf.io.FixedLenFeature([], tf.int64),
    }
    parsed_example = tf.io.parse_single_example(tfrecord, feature_description)

    # 이미지 디코딩 및 전처리
    image = tf.image.decode_jpeg(parsed_example["image"], channels=3)
    image = tf.image.resize(image, [224, 224])  # MobileNetV2 입력 크기에 맞추기
    image = tf.keras.applications.mobilenet_v2.preprocess_input(image)  # MobileNetV2 전처리

    # 레이블 설정
    label = tf.cast(parsed_example["target"], tf.int32)

    return image, label

def load_fashion_dataset(tfrecord_files, batch_size=32):
    dataset = tf.data.TFRecordDataset(tfrecord_files)
    dataset = dataset.map(parse_fashion_tfrecord, num_parallel_calls=tf.data.AUTOTUNE)
    dataset = dataset.batch(batch_size)
    dataset = dataset.prefetch(buffer_size=tf.data.AUTOTUNE)
    return dataset

# 평가 데이터셋 로드
base_dir = "/Users/Heily/Desktop/WeeSeed_Visual/pythonProject/pythonProject1/static/Fashion Dataset"
test_fashion_tfrecord_files = glob.glob(os.path.join(base_dir, "test*.tfrec"))
test_dataset = load_fashion_dataset(test_fashion_tfrecord_files)

# Precision, Recall, F1 Score 계산을 위한 Metric 정의
precision_metric = tf.keras.metrics.Precision()
recall_metric = tf.keras.metrics.Recall()

# y_true와 y_pred 생성
y_true = []
y_pred = []

# 평가 데이터셋에서 예측 수행
for images, labels in test_dataset:
    predictions = model.predict(images)
    y_true.extend(labels.numpy())
    y_pred.extend(tf.argmax(predictions, axis=1).numpy())

# Precision과 Recall 업데이트
precision_metric.update_state(y_true, y_pred)
recall_metric.update_state(y_true, y_pred)

# Precision, Recall, F1 Score 출력
precision = precision_metric.result().numpy()
recall = recall_metric.result().numpy()
f1_score = 2 * (precision * recall) / (precision + recall + tf.keras.backend.epsilon())

print(f"Precision: {precision:.2f}")
print(f"Recall: {recall:.2f}")
print(f"F1 Score: {f1_score:.2f}")