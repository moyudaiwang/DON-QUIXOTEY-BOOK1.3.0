import OSS from 'aliyun-oss-js-sdk';
import { Message } from 'element-ui';

/**
 * 阿里云OSS工具类
 * 封装了文件上传、下载、删除等常用操作
 */
class OSSUtil {
  constructor() {
    // OSS配置信息
    this.config = {
      region: 'oss-cn-hangzhou', // OSS区域
      accessKeyId: 'your-access-key-id', // 访问密钥ID
      accessKeySecret: 'your-access-key-secret', // 访问密钥密码
      bucket: 'your-bucket-name', // OSS的Bucket名称
    };
    
    // 初始化OSS客户端
    this.client = null;
    this.init();
  }

  /**
   * 初始化OSS客户端
   */
  init() {
    try {
      this.client = new OSS({
        region: this.config.region,
        accessKeyId: this.config.accessKeyId,
        accessKeySecret: this.config.accessKeySecret,
        bucket: this.config.bucket,
      });
    } catch (error) {
      console.error('OSS客户端初始化失败:', error);
      Message.error('OSS客户端初始化失败');
    }
  }

  /**
   * 生成文件路径
   * @param {String} fileName 文件名
   * @param {String} dir 目录
   * @returns {String} 完整的文件路径
   */
  generatePath(fileName, dir = 'upload') {
    const date = new Date();
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${dir}/${year}${month}${day}/${Date.now()}-${fileName}`;
  }

  /**
   * 上传单个文件
   * @param {File} file 文件对象
   * @param {String} dir 存储目录
   * @returns {Promise} 上传结果Promise对象
   */
  async uploadFile(file, dir) {
    try {
      if (!this.client) {
        throw new Error('OSS客户端未初始化');
      }

      const path = this.generatePath(file.name, dir);
      const result = await this.client.put(path, file);

      if (result.res.status === 200) {
        return {
          success: true,
          url: result.url,
          path: path
        };
      } else {
        throw new Error('文件上传失败');
      }
    } catch (error) {
      console.error('文件上传错误:', error);
      Message.error(error.message || '文件上传失败');
      return {
        success: false,
        error: error.message
      };
    }
  }

  /**
   * 批量上传文件
   * @param {Array<File>} files 文件对象数组
   * @param {String} dir 存储目录
   * @returns {Promise} 上传结果Promise对象
   */
  async uploadFiles(files, dir) {
    try {
      const uploadPromises = files.map(file => this.uploadFile(file, dir));
      const results = await Promise.all(uploadPromises);
      return {
        success: true,
        results: results
      };
    } catch (error) {
      console.error('批量上传错误:', error);
      Message.error('批量上传失败');
      return {
        success: false,
        error: error.message
      };
    }
  }

  /**
   * 下载文件
   * @param {String} objectName 文件路径
   * @param {String} downloadName 下载文件名
   * @returns {Promise} 下载结果Promise对象
   */
  async downloadFile(objectName, downloadName) {
    try {
      if (!this.client) {
        throw new Error('OSS客户端未初始化');
      }

      const result = await this.client.get(objectName);
      if (result.res.status === 200) {
        // 创建Blob对象
        const blob = new Blob([result.content]);
        // 创建下载链接
        const link = document.createElement('a');
        link.href = window.URL.createObjectURL(blob);
        link.download = downloadName || objectName.split('/').pop();
        link.click();
        // 释放URL对象
        window.URL.revokeObjectURL(link.href);
        return {
          success: true
        };
      } else {
        throw new Error('文件下载失败');
      }
    } catch (error) {
      console.error('文件下载错误:', error);
      Message.error(error.message || '文件下载失败');
      return {
        success: false,
        error: error.message
      };
    }
  }

  /**
   * 删除文件
   * @param {String} objectName 文件路径
   * @returns {Promise} 删除结果Promise对象
   */
  async deleteFile(objectName) {
    try {
      if (!this.client) {
        throw new Error('OSS客户端未初始化');
      }

      const result = await this.client.delete(objectName);
      if (result.res.status === 204) {
        return {
          success: true
        };
      } else {
        throw new Error('文件删除失败');
      }
    } catch (error) {
      console.error('文件删除错误:', error);
      Message.error(error.message || '文件删除失败');
      return {
        success: false,
        error: error.message
      };
    }
  }

  /**
   * 批量删除文件
   * @param {Array<String>} objectNames 文件路径数组
   * @returns {Promise} 删除结果Promise对象
   */
  async deleteFiles(objectNames) {
    try {
      const deletePromises = objectNames.map(objectName => this.deleteFile(objectName));
      const results = await Promise.all(deletePromises);
      return {
        success: true,
        results: results
      };
    } catch (error) {
      console.error('批量删除错误:', error);
      Message.error('批量删除失败');
      return {
        success: false,
        error: error.message
      };
    }
  }

  /**
   * 获取文件访问URL
   * @param {String} objectName 文件路径
   * @param {Number} expires URL有效期(秒)
   * @returns {Promise} 文件URL
   */
  async getFileUrl(objectName, expires = 3600) {
    try {
      if (!this.client) {
        throw new Error('OSS客户端未初始化');
      }

      const url = await this.client.signatureUrl(objectName, {
        expires: expires
      });

      return {
        success: true,
        url: url
      };
    } catch (error) {
      console.error('获取文件URL错误:', error);
      Message.error(error.message || '获取文件URL失败');
      return {
        success: false,
        error: error.message
      };
    }
  }
}

export default new OSSUtil();