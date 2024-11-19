from flask import Flask, request, send_file
from flask_cors import CORS
from transformers import WhisperProcessor, WhisperForConditionalGeneration
import torch
import librosa
from io import BytesIO
import subprocess
import difflib
import gtts

app=Flask(__name__)
cors = CORS(app, resources={r"/api/*": {"origins": "*"}})

@app.route('/api/soundcompare', methods=['POST'])
def upload_file():
    if 'audio' not in request.files:
        return "파일이 없습니다.", 400

    audio_file = request.files['audio']
    
    if audio_file.filename == '':
        return "파일이 없습니다.", 400

    # 카드명 텍스트 받기
    card_name = request.form.get('card_name')

    # 샘플링레이트와 확장자 변환 & 재인코딩
    wav_bytes=ReEncodeToWav(BytesIO(audio_file.read()))
    result=soundCompare(wav_bytes)

    print(result)
    # 카드명 텍스트 확인
    print(f"Card Name: {card_name}")

    return get_similarity(result, card_name), 200

@app.route('/api/tts', methods=['POST'])
def return_tts():
    # 카드명 텍스트 받기
    card_name = request.args.get('cardName')

    language_type="ko"
    text_value=card_name

    speech=gtts.gTTS(
        lang=language_type,
        text=text_value,
        slow=False
    )

    file_name=card_name+".mp3"

    speech_file = BytesIO()
    speech.write_to_fp(speech_file)
    
    # 파일 포인터를 맨 앞으로 이동
    speech_file.seek(0)
    print(file_name)

    return send_file(speech_file, mimetype='audio/mpeg', as_attachment=True, download_name=file_name)
    

@app.route('/api/hello')
def hello():
    return "hello",200

def ReEncodeToWav(input_bytes, sample_rate=16000):
    # ffmpeg 명령어 설정
    command = [
        "ffmpeg",
        "-i", "pipe:0",  # 입력은 stdin으로부터
        "-ar", str(sample_rate),  # 샘플링 레이트 설정
        "-f", "wav",
        "pipe:1"  # 출력은 stdout으로
    ]

    try:
        # subprocess를 사용하여 ffmpeg 명령어 실행
        process = subprocess.Popen(
            command,
            stdin=subprocess.PIPE,
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE
        )

        # 입력 데이터 스트림을 ffmpeg 프로세스의 stdin으로 보내기
        stdout, stderr = process.communicate(input=input_bytes.getvalue())
        
        if process.returncode != 0:
            print(f"ffmpeg 에러: {stderr.decode('utf-8')}")
            return None
        
        # 재인코딩된 데이터를 BytesIO로 감싸서 반환
        return BytesIO(stdout)
    except Exception as e:
        print(f"재인코딩 실패: {e}")
        return None

def soundCompare(wav_bytes): #change model to bigger one.

    audio, sr = librosa.load(wav_bytes, sr=None)

    input_features = processor(audio,sampling_rate=sr ,return_tensors="pt").input_features

    with torch.no_grad():
        predicted_ids = model.generate(input_features, language='ko')

    # 텍스트 변환
    transcription = processor.batch_decode(predicted_ids, skip_special_tokens=True)

    return transcription[0] 

def get_similarity(input, answer):
    answer_bytes = bytes(answer, 'utf-8')
    input_bytes = bytes(input, 'utf-8')
    answer_bytes_list = list(answer_bytes)
    input_bytes_list = list(input_bytes)

    sm = difflib.SequenceMatcher(None, answer_bytes_list, input_bytes_list)
    similar = sm.ratio()*100
    print(similar)

    return str(similar)

@app.route('/api/soundcompare/ok', methods=['POST'])
def test_ok():
    if 'audio' not in request.files:
        return "파일이 없습니다.", 400

    audio_file = request.files['audio']
    
    if audio_file.filename == '':
        return "파일이 없습니다.", 400

    # 응답으로 임의의 점수 반환 (테스트용)
    return "70", 200

@app.route('/api/soundcompare/notok', methods=['POST'])
def test_notok():
    if 'audio' not in request.files:
        return "파일이 없습니다.", 400

    audio_file = request.files['audio']
    
    if audio_file.filename == '':
        return "파일이 없습니다.", 400

    # 응답으로 임의의 점수 반환 (테스트용)
    return "20", 200


@app.route('/api/soundcompare/test/<param>', methods=['POST'])
def test_echo(param):
    if 'audio' not in request.files:
        return "파일이 없습니다.", 400
    
    audio_file = request.files['audio']
    
    if audio_file.filename == '':
        return "파일이 없습니다.", 400

    print(param)
    # 응답으로 임의의 점수 반환 (테스트용)
    return str(param), 200

if __name__ == "__main__":
    
    #GPU할당
    device = "cuda:0" if torch.cuda.is_available() else "cpu"

    #모델 불러오기
    path='whisper\\'
    model_path="model"
    processor_path="processor"
    model = WhisperForConditionalGeneration.from_pretrained(path+model_path)
    processor = WhisperProcessor.from_pretrained(path+processor_path)
    model.to(device)

    model.config.forced_decoder_ids = processor.get_decoder_prompt_ids(language="ko", task="transcribe")

    #웹서버 실행
    app.run(host='0.0.0.0', debug=True)
