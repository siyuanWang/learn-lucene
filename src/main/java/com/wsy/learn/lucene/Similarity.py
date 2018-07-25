#coding=utf-8
import math

"""
docCount：文档数量
docFreq:document frequency 文档频率
freq:frequency文档中的词频
k1:参数k1,默认1.2
b:参数b,默认0.75
fieldLength:doc term 长度
avgFieldLength:平均term sum(term length)/docCount
"""
def bm25(docCount, docFreq, freq, k1, b, fieldLength, avgFieldLength):
    idf = math.log(1 + (docCount - docFreq + 0.5)/(docFreq + 0.5))
    print(idf)
    tfNorm = (freq * (k1 + 1)) / (freq + k1 * (1 - b + b * fieldLength / avgFieldLength))
    print(tfNorm)
    score = idf*tfNorm
    print(score)
"""
余弦相似度例子
"""
def cosSim():
    d1 = [0,1,1,0,0,0,0]
    d2 = [0,1,1,0,0,0,0]
    q = [0,1,1,0,0,0,0]

    sim1 = cosine_similarity(q, d1)
    sim2 = cosine_similarity(q, d2)
    print("sim1=" + str(sim1))
    print("sim2=" + str(sim2))

def cosine_similarity(vector1, vector2):
    dot_product = 0.0
    normA = 0.0
    normB = 0.0
    for a, b in zip(vector1, vector2):
        dot_product += a * b
        normA += a ** 2
        normB += b ** 2
    if normA == 0.0 or normB == 0.0:
        return 0
    else:
        return round(dot_product / ((normA**0.5)*(normB**0.5)) * 100, 2)

if __name__ == '__main__':
    #bm25(12, 1, 1,1.2,0.75,13,6.3333335)
    cosSim()