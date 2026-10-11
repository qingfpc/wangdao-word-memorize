import { AuthUser } from "@hw-agconnect/auth";
import {
  userPrivilegeReviewAiRead,
  userPrivilegeReviewAiReadFree,
  userPrivilegeReviewAiReadOptions
} from "../../api/privilege"
import { Constants } from "../../common/Constants";
import { AiReadOption, AiReadSituation } from "../../model/AiReadOption"
import { AiReadParam } from "../../model/AiReadParam";
import { CommonResult } from "../../model/CommonResult";
import { VocabularyReviewService } from "../../service/VocabularyReviewService";
import { AiReadVocabularyListComponent } from "./AiReadVocabularyListComponent";
import lottie, { AnimationItem } from '@ohos/lottie';
import { SegmentModel } from "../../model/SegmentModel";

@Component
export struct AiTab {
  @State aiReadSituations: AiReadSituation[] = [];
  @State selectedSituation: AiReadSituation | null = null;
  @State selectedOption: AiReadOption | null = null;
  @State isLoading: boolean = true;
  @State freeAiCount: number = 0;
  @Consume authUser: AuthUser
  @State showVocList: boolean = true;
  @State showSegmentList: boolean = true;
  // 选择的词语
  @State selectedVocs: string[] = []
  @State revVocs: string[] = []

  vocRevService: VocabularyReviewService = new VocabularyReviewService();
  // 全部词汇
  @Consume @Watch('updateVocs') currentIndex: number
  @Consume selectVocabularyLeadingToAiVocChange:boolean

  @State loading:boolean = false;
  @State generated:boolean = false;
  @State segmentList:SegmentModel[] = []

  updateVocs() {
    if (this.currentIndex === Constants.AI_TAB_INDEX && this.selectVocabularyLeadingToAiVocChange) {
      console.info(`ai_tab`)
      this.selectedVocs = []
      this.revVocs = this.vocRevService.getAll(this.authUser.getUid())
      this.selectVocabularyLeadingToAiVocChange = false
    }
  }

  async aboutToAppear() {
    try {
      const result = await userPrivilegeReviewAiReadOptions();
      if (result.code === 200) {
        const data = result.data as AiReadSituation[];
        if (data?.length) {
          this.aiReadSituations = data;
          this.selectedSituation = data[0];
        }
      }
      const result2: CommonResult = await userPrivilegeReviewAiReadFree()
      if (result2.code === 200) {
        let str = JSON.stringify(result2.data)
        this.freeAiCount = JSON.parse(str);
      }
      this.revVocs = this.vocRevService.getAll(this.authUser.getUid())
    } catch (error) {
      console.error('数据加载失败:', error);
    } finally {
      this.isLoading = false;
    }
  }

  onSituationChange(situation: AiReadSituation) {
    animateTo({ duration: 200 }, () => {
      this.selectedSituation = situation;
      this.selectedOption = null;
    });
  }

  onOptionSelect(option: AiReadOption) {
    animateTo({ duration: 100 }, () => {
      this.selectedOption = option;
    });
  }

  build() {
    Column() {
      if (this.isLoading) {
        // 加载状态
        Column() {
          Row() {
            ForEach([1, 2, 3], () => {
              Rect()
                .width(80)
                .height(28)
                .radius(4)
                .fill('#EEE')
                .margin({ right: 8 })
            })
          }

          Row() {
            ForEach([1, 2, 3], () => {
              Rect()
                .width(100)
                .height(24)
                .radius(4)
                .fill('#F5F5F5')
                .margin({ right: 6 })
            })
          }
        }
        .padding(16)
      } else if (this.aiReadSituations.length === 0) {
        // 空状态
        Text("暂无可用情境")
          .fontSize(14)
          .fontColor('#999')
          .margin(20)
      } else {
        // 主内容
        Column() {
          // 情境选项卡
          Flex({ wrap: FlexWrap.Wrap,direction:FlexDirection.Row,justifyContent:FlexAlign.SpaceBetween }) {
            Row() {

              ForEach(this.aiReadSituations, (situation: AiReadSituation) => {
                Button(situation.name)
                  .backgroundColor(
                    this.selectedSituation?.id === situation.id
                      ? $r('app.color.words_base')
                      : Color.Transparent
                  )
                  .fontColor(
                    this.selectedSituation?.id === situation.id
                      ? Color.White
                      : '#666666'
                  )
                  .border({
                    width: this.selectedSituation?.id === situation.id ? 2 : 1,
                    color: this.selectedSituation?.id === situation.id
                      ? $r('app.color.words_base')
                      : '#DDDDDD',
                    style: BorderStyle.Solid
                  })
                  .fontSize(12)
                  .height(28)
                  .padding(2)
                  .margin({ right: 8 })
                  .onClick(() => this.onSituationChange(situation))
                  .animation({ duration: 100, curve: Curve.EaseInOut })
              })
            }

            Row() {
              Text('免费次数')
                .fontSize(14)
                .fontColor('#999')
                .margin({ right: 8 }) // 添加右边距分隔文字和数字

              // 基础样式方案
              Text(`${this.freeAiCount}`)
                .fontSize(17)
                .fontColor('#FF6600')
                .fontWeight(FontWeight.Bold)
                .textShadow({
                  radius: 2,
                  color: Color.Orange,
                  offsetX: 1,
                  offsetY: 1
                })
              Button('生成')
                .width(60)
                .height(32)
                .fontSize(14)
                .fontColor('#FFFFFF')
                .backgroundColor($r('app.color.words_base'))
                .borderRadius(2)
                .stateEffect(true)// 启用按压效果
                .opacity(this.selectedOption ? 1 : 0.6)// 未选择时半透明
                .margin({ left: 8 })// 与左侧元素间距
                .onClick(async () => {
                  if (!this.selectedOption || !this.selectedOption) {
                    AlertDialog.show({ message: '请先选择情境和选项' })
                    return;
                  }
                  try {
                    this.showVocList = false
                    this.generated = false
                    this.loading = true
                    let param = {
                      vocList: this.selectedVocs,
                      situationName: this.selectedSituation!.name,
                      optionName: this.selectedOption!.name
                    } as AiReadParam
                    let result = await userPrivilegeReviewAiRead(param)
                    if (result.code === 200) {
                      this.loading = false
                      this.generated = true
                      //if (result.data) {
                        //console.info(JSON.stringify(result.data))
                      //}
                      this.segmentList = result.data as SegmentModel[]
                    }
                  } catch (e) {
                    console.error('生成失败', e)
                  }
                })
            }
            .width('100%')
            .justifyContent(FlexAlign.Start)
            .padding(4)
          }
          .padding({ top: 4, bottom: 4 })
          .width('100%')



          // 选项选项卡
          if (this.selectedSituation) {
            Column() {
              if (this.selectedSituation.options.length === 0) {
                Text("当前情境下没有可用选项")
                  .fontSize(12)
                  .fontColor('#999')
              } else {
                Row() {
                  ForEach(this.selectedSituation.options, (option: AiReadOption) => {
                    Button(option.name)
                      .backgroundColor(
                        this.selectedOption?.id === option.id
                          ? '#E6F2FF'
                          : '#FAFAFA'
                      )
                      .fontColor(
                        this.selectedOption?.id === option.id
                          ? $r('app.color.words_base')
                          : '#888888'
                      )
                      .borderRadius(2)
                      .fontSize(12)
                      .height(24)
                      .padding(6)
                      .margin({ right: 6 })
                      .onClick(() => this.onOptionSelect(option))
                      .animation({ duration: 100, curve: Curve.EaseInOut })
                  })
                }
              }
            }
            .padding({ bottom: 4 })
          }

          // 详细信息
          if (this.selectedOption) {
            Text(`${this.selectedOption.description}`)
              .fontSize(14)
              .fontColor('#333')
              .margin({ top: 12, left: 16 })
              .transition({ type: TransitionType.Insert, opacity: 0.99 })
          }
          Row(){
            Text(`单词列表`)
              .margin({right:12})
            Image($r('app.media.daosanjiao'))
              .fillColor($r('app.color.words_base'))
              .width(24)
              .aspectRatio(1)
              .onClick(()=>{
                this.showVocList = !this.showVocList
              })
          }
          Scroll(){
            Column(){
              // 关联画布
              if (this.loading) {

                Canvas(this.canvasRenderingContext)
                  .width(200)
                  .height(200)
                  .onReady(() => {
                    // 加载动画
                    if (this.animateItem != null) {
                      // 可在此生命回调周期中加载动画，可以保证动画尺寸正确
                      this.animateItem?.resize();
                    } else {
                      // 抗锯齿的设置
                      this.canvasRenderingContext.imageSmoothingEnabled = true;
                      this.canvasRenderingContext.imageSmoothingQuality = 'medium'
                      this.loadAnimation();
                    }
                  })
              }
              if (this.showVocList) {
                // 单词列表
                AiReadVocabularyListComponent({ selectedVocs: this.selectedVocs, revVocs: this.revVocs })
              }
              if (this.generated && !this.loading) {
                Row(){
                  Text(`阅读内容`)
                   .margin({right:12})
                  Image($r('app.media.daosanjiao'))
                    .fillColor($r('app.color.words_base'))
                   .width(24)
                   .aspectRatio(1)
                   .onClick(()=>{
                      this.showSegmentList =!this.showSegmentList
                    })
                }
              }
              if (this.showSegmentList) {
                List() {
                  ForEach(this.segmentList, (item: SegmentModel) => {
                    ListItem() {
                      RichText(`<h1>${item.segment}</h1>`)
                        .height(60)
                        .width('100%')
                    }

                    ListItem() {
                      RichText(`<h1>${item.segmentTranslation}</h1>`)
                        .height(60)
                        .width('100%')
                    }
                  })
                }
              }
            }
          }
          .scrollBar(BarState.Off)
        }
        .padding(16)
      }
    }
    .width('100%')
    .height('100%')
    .backgroundColor(Color.White)
  }

  // 构建上下文
  private renderingSettings: RenderingContextSettings = new RenderingContextSettings(true)
  private canvasRenderingContext: CanvasRenderingContext2D = new CanvasRenderingContext2D(this.renderingSettings)
  private animateItem: AnimationItem | null = null;
  private animateName: string = "loading"; // 动画名称
  loadAnimation() {
    this.animateItem = lottie.loadAnimation({
      container: this.canvasRenderingContext,
      renderer: 'canvas', // canvas 渲染模式
      loop: true,
      autoplay: false,
      name: this.animateName,
      contentMode: 'Contain',
      path: "common/lottie/loading.json",
    })
    // 因为动画是异步加载，所以对animateItem的操作需要放在动画加载完成回调里操作
    this.animateItem.addEventListener('DOMLoaded', (args: Object): void => {
      this.animateItem!.changeColor([51, 26, 254, 1]);
      this.animateItem!.play()
    });
  }

  destroy() {
    this.animateItem!.removeEventListener("DOMLoaded");
    lottie.destroy(this.animateName);
    this.animateItem = null;
  }
}


