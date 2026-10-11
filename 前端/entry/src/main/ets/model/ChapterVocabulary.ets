

export class Interpretation {
  content: string = '';
  id: number = 0;
  type: string = '';

  constructor(content?: string, id?: number, type?: string) {
    if (content) this.content = content;
    if (id) this.id = id;
    if (type) this.type = type;
  }
}

export class Vocabulary {
  content: string = '';
  id: string = '';
  interpretations: Interpretation[] = [];
  notRemember: number = 0;
  reviewed: number = 0;

  constructor(
    content?: string,
    id?: string,
    interpretations?: Interpretation[],
    notRemember?: number,
    reviewed?: number
  ) {
    if (content) this.content = content;
    if (id) this.id = id;
    if (interpretations) this.interpretations = interpretations;
    if (notRemember) this.notRemember = notRemember;
    if (reviewed) this.reviewed = reviewed;
  }
}

export class Chapter {
  chapterName: string = '';
  displayVocs: Vocabulary[] = [];
  id: string = '';

  constructor(chapterName?: string, displayVocs?: Vocabulary[], id?: string) {
    if (chapterName) this.chapterName = chapterName;
    if (displayVocs) this.displayVocs = displayVocs;
    if (id) this.id = id;
  }
}

export class VocabularyReview extends Vocabulary {
  vocRevId:string = ''
}