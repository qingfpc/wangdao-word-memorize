/**
 * 单词数据接口，整合了单词的各种信息，包括内容、衍生 ID、示例、解释、记忆状态等
 */
export interface VocabularyData {
  // 单词的具体内容
  content: string;
  // 单词的衍生 ID
  derivedId: string;
  // 单词的示例列表
  examples: Example[];
  // 单词的唯一标识符
  id: string;
  // 单词的解释列表
  interpretations: Interpretation[];
  // 标记是否未记住该单词，0 表示记住，非 0 表示未记住
  notRemember: number;
  // 标记该单词是否已复习，0 表示未复习，非 0 表示已复习
  reviewed: number;
  favorite: number;
  // 英式音标
  ukPhonetic: string;
  // 英式发音音频链接
  ukPronounciation: string;
  // 美式音标
  usPhonetic: string;
  // 美式发音音频链接
  usPronounciation: string;
  // 用户提供的示例列表
  userExamples: UserExample[];
  // 用户提供的解释列表
  userInterpretations: UserInterpretation[];
}

/**
 * 示例数据接口，包含示例内容、ID、含义和词汇 ID
 */
export interface Example {
  // 示例的具体内容
  content: string;
  // 示例的唯一标识符
  id: string;
  // 示例对应的含义
  meaning: string;
  // 关联的词汇 ID
  vocId: string;
}

/**
 * 解释数据接口，包含解释的 ID、具体内容、类型和关联的词汇 ID
 */
export interface Interpretation {
  // 解释的唯一标识符
  id: string;
  // 解释的具体内容
  interContent: string;
  // 解释的类型
  type: string;
  // 关联的词汇 ID
  vocId: string;
}

/**
 * 用户示例数据接口，包含用户示例的 ID、含义、句子、用户 ID、词汇内容和词汇 ID
 */
export interface UserExample {
  // 用户示例的唯一标识符
  id: string;
  // 用户示例对应的含义
  meaning: string;
  // 用户提供的句子示例
  sentence: string;
  // 创建该示例的用户 ID
  userId: string;
  // 关联的词汇内容
  vocContent: string;
  // 关联的词汇 ID
  vocId: string;
}

/**
 * 用户解释数据接口，包含用户解释的 ID、含义和类型
 */
export interface UserInterpretation {
  // 用户解释的唯一标识符
  id: number;
  // 用户解释的含义
  meaning: string;
  // 用户解释的类型
  type: string;
}