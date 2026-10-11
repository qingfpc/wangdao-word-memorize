
@Observed
export class MineInfo {
  birthday?: string = "";
  email?: string = "";
  hasSignCard?: number = 0;
  headImg?: string = "";
  id?: number = 0;
  levelDate?: string = "";
  levelName?: string = "";
  levelValue?: number = 0;
  nickName?: string = "";
  phoneNumber?: string = "";
  privilegeCodes?: string[] = [];
  statement?: string = "";
  thirdPartyName?: string = "";
  vocCountOfDay?: number = 0;

  constructor(birthday?: string, email?: string,
    hasSignCard?: number,
    headImg?: string,
    id?: number,
    levelDate?: string,
    levelName?: string,
    levelValue?: number,
    nickName?: string,
    phoneNumber?: string,
    privilegeCodes?: string[],
    statement?: string,
    thirdPartyName?: string,
    vocCountOfDay?: number) {
    this.birthday = birthday;
    this.email = email;
    this.hasSignCard = hasSignCard;
    this.headImg = headImg;
    this.id = id;
    this.levelDate = levelDate;
    this.levelName = levelName;
    this.levelValue = levelValue;
    this.nickName = nickName;
    this.phoneNumber = phoneNumber;
    this.privilegeCodes = privilegeCodes;
    this.statement = statement;
    this.thirdPartyName = thirdPartyName;
    this.vocCountOfDay = vocCountOfDay;
  }
}
