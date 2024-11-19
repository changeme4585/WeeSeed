import tensorflow as tf
from tensorflow.keras.applications import MobileNetV2
from tensorflow.keras import layers, models
import glob
import os
import matplotlib.pyplot as plt

# TFRecord 파일 경로 설정
base_dir = "/Users/Heily/Desktop/WeeSeed_Visual/pythonProject/pythonProject1/static/Fashion Dataset"
train_fashion_tfrecord_files = glob.glob(os.path.join(base_dir, "train*.tfrec"))
test_fashion_tfrecord_files = glob.glob(os.path.join(base_dir, "test*.tfrec"))

# TFRecord 파싱 함수 정의
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

# 데이터셋 로드 함수 정의
def load_fashion_dataset(tfrecord_files, batch_size=32):
    dataset = tf.data.TFRecordDataset(tfrecord_files)
    dataset = dataset.map(parse_fashion_tfrecord, num_parallel_calls=tf.data.AUTOTUNE)
    dataset = dataset.batch(batch_size)
    dataset = dataset.prefetch(buffer_size=tf.data.AUTOTUNE)
    return dataset

# 학습 및 검증 데이터셋 로드
train_dataset = load_fashion_dataset(train_fashion_tfrecord_files)

# 20%를 validation으로 분리
valid_size = int(0.2 * 1250)
valid_dataset = train_dataset.take(valid_size)  # 상위 20% 추출
train_dataset = train_dataset.skip(valid_size)  # 나머지 80%

# 각 데이터셋을 캐싱하고 prefetch를 추가 (성능 최적화)
train_dataset = train_dataset.cache().prefetch(buffer_size=tf.data.AUTOTUNE)
valid_dataset = valid_dataset.cache().prefetch(buffer_size=tf.data.AUTOTUNE)

# train_dataset = load_fashion_dataset(train_fashion_tfrecord_files)
test_dataset = load_fashion_dataset(test_fashion_tfrecord_files)

# MobileNetV2 모델 구성
base_model = MobileNetV2(input_shape=(224, 224, 3), include_top=False, weights="imagenet")
base_model.trainable = False  # 사전 학습된 가중치를 고정하여 상위 레이어만 훈련

# 모델 정의
model = models.Sequential([
    base_model,
    layers.GlobalAveragePooling2D(),
    layers.Dense(7, activation="softmax")  # 클래스 수에 맞추어 조정 (예: 6개의 클래스)
])

# 모델 컴파일
model.compile(optimizer='adam', loss='sparse_categorical_crossentropy', metrics=['accuracy'])

# 모델 학습
model.fit(train_dataset, validation_data=valid_dataset, epochs=20)

# 모델 평가
test_loss, test_accuracy = model.evaluate(test_dataset)
print(f"Test accuracy: {test_accuracy:.2f}")

# 모델 저장
model_save_path = "/Users/Heily/Desktop/WeeSeed_Visual/pythonProject/pythonProject1/saved_models/my_fashion_model_train_model"
# model.save(model_save_path)
tf.keras.models.save_model(model, model_save_path, save_format='tf')
