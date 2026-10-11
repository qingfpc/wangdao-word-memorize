
/**
 * 	"userId": "",
 "vocId": "",
 "vocContent": "",
 "sentences": [
 {
 "id": "",
 "sentence": "",
 "meaning": ""
 }
 ]
 */
export interface WordSentence  {
  userId: string;
  vocId: string;
  vocContent: string;
  sentences: Sentence[];
}
export interface Sentence {
  id: string;
  sentence: string;
  meaning: string;
}