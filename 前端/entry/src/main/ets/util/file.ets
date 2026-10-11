import resourceManager from "@ohos.resourceManager";
import { fileIo } from "@kit.CoreFileKit";
import { GlobalContext } from "../util/GlobalContext";

export function copyFile2Sandbox(fileName:string) {
  const context = GlobalContext.getContext().getObject('context') as Context
  let progress: number = 0;
  // 拿到原始文件路径
  console.info(fileName)
  const data: resourceManager.RawFileDescriptor = context.resourceManager.getRawFdSync(fileName);
  // 保存文件路径
  const targetPath: string = context.databaseDir + '/rdb/' + fileName;
  // 判断是否有操作路径的，没有的话创建文件夹
  if (!fileIo.accessSync(context.databaseDir + '/rdb/')){
    fileIo.mkdirSync(context.databaseDir + '/rdb/')
  }
  const destFile: fileIo.File = fileIo.openSync(targetPath, fileIo.OpenMode.CREATE | fileIo.OpenMode.READ_WRITE);
  let buffSize: number = 4096;
  const buffer: ArrayBuffer = new ArrayBuffer(Math.min(buffSize, data.length));
  // Offset of the record read position (compared with the start offset of the file)
  let off: number = 0;
  // Actual length of the read content.
  let len: number = fileIo.readSync(data.fd, buffer, { offset: data.offset + off, length: buffSize });
  // Record the length of the read file.
  let readLen: number = 0;
  while (len > 0) {
    readLen += len;
    fileIo.writeSync(destFile.fd, buffer, { offset: off, length: len });
    progress = Math.floor((readLen / data.length) * 100);
    off = off + len;
    if ((data.length - readLen) < buffSize) {
      buffSize = data.length - readLen;
    }
    len = fileIo.readSync(data.fd, buffer, { offset: data.offset + off, length: buffSize });
  }
  fileIo.close(destFile.fd);
}