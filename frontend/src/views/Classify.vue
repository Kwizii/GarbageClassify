<template>
  <a-card :bordered="false">
    <a-page-header
      title="在线识别垃圾分类"
      sub-title="请在下方上传需要检测的图片"
    ><template slot="tags">
       <a-tag color="blue">
         <a-popover>
           <template slot="content">
             <p>可回收物：充电宝、包、化妆品瓶、塑料玩具、塑料碗盆、塑料衣架、快递纸袋、插头电线、旧衣服、易拉罐、枕头、毛绒玩具、洗发水瓶、玻璃杯、皮鞋、砧板、纸板箱、调料瓶、酒瓶、金属食品罐、锅、食用油桶、饮料瓶</p>
             <p>厨余垃圾：剩饭剩菜、大骨头、水果果皮、水果果肉、茶叶渣、菜叶菜根、蛋壳、鱼骨</p>
             <p>有害垃圾：干电池、软膏、过期药物</p>
             <p>其他垃圾：一次性快餐盒、污损塑料、烟蒂、牙签、破碎花盆及碟碗、竹筷</p>
           </template>
           支持的类别
         </a-popover>
       </a-tag>
     </template>
      <template slot="extra">
        <a-row v-if="preds.length" type="flex">
          <a-statistic
            :value-style="{ color: preds[0].color }"
            title="类别"
            :value="preds[0].cls"
            :formatter="rawFormat" />
          <a-statistic
            title="物品"
            :value="preds[0].name"
            :style="{
              margin: '0 8px',
            }"
            :formatter="rawFormat"
          />
          <a-statistic title="置信度(越高越准确)" :value="preds[0].conf" :precision="2"/>
        </a-row>
      </template>
    </a-page-header>
    <a-button
      size="large"
      @click="handleClassify"
      icon="search"
      v-if="imageUrl.length"
      type="primary"
      style="margin-bottom: 10px;"
      block>检测</a-button>
    <a-upload-dragger
      name="avatar"
      accept=".jpg, .jpeg, .png"
      class="avatar-uploader"
      :show-upload-list="false"
      :before-upload="beforeUpload"
      :customRequest="customRequest"
      :multiple="false"
      @change="handleChange"
    >
      <img style="width: 100%;" v-if="imageUrl" :src="imageUrl" alt="avatar"/>
      <div style="margin: 55px;" v-else>
        <a-icon :type="loading ? 'loading' : 'plus'"/>
        <div class="ant-upload-text">
          拖拽图片至此处或点击上传
        </div>
      </div>
    </a-upload-dragger>
  </a-card>
</template>

<script>

import { classify } from '../api/model'
import { uploadImage } from '../api/file'

function getBase64 (img, callback) {
  const reader = new FileReader()
  reader.addEventListener('load', () => callback(reader.result))
  reader.readAsDataURL(img)
}

const clsColors = {
  '可回收物': 'rgba(0, 123, 255, 0.85)', // 蓝色
  '有害垃圾': 'rgba(220, 53, 69, 0.85)', // 红色
  '厨余垃圾': 'rgba(40, 167, 69, 0.85)', // 绿色
  '其他垃圾': 'rgba(108, 117, 125, 0.85)' // 灰色
}

export default {
  name: 'Classify',
  data () {
    return {
      loading: false,
      imageUrl: '',
      file: null,
      preds: [{ 'cls': '-', 'conf': '0', 'name': '-', 'color': 'rgba(0, 0, 0, 0.85)' }]
    }
  },
  methods: {
    rawFormat ({ value }) {
      return value.toString()
    },
    handleChange (info) {
      console.log(info)
      if (info.file.status === 'uploading') {
        this.loading = true
      }
    },
    beforeUpload (file) {
      const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png'
      if (!isJpgOrPng) {
        this.$message.error('只能上传JPG或PNG格式图片')
      }
      const isLt10M = file.size / 1024 / 1024 < 10
      if (!isLt10M) {
        this.$message.error('图片不得大于10MB')
      }
      return isJpgOrPng && isLt10M
    },
    customRequest ({ file }) {
      this.file = file
      getBase64(file, imageUrl => {
        this.imageUrl = imageUrl
        this.loading = false
      })
    },
    handleClassify () {
      if (this.file === null) {
        this.$notification.error({
          message: '错误',
          description: '文件异常'
        })
      }
      uploadImage(this.file).then(res => {
        console.log(res)
      })
      const formData = new FormData()
      formData.append('file', this.file)
      classify(formData).then(resp => {
        resp.data.forEach(pred => {
          const splited = pred['cls'].split('/')
          pred['color'] = clsColors[splited[0]]
          pred['cls'] = splited[0]
          pred['name'] = splited[1]
        })
        this.preds = resp.data
        this.$notification.success({
          message: '检测成功'
        })
      }).catch(err => {
        this.$notification.error({
          message: '检测错误',
          description: err
        })
      })
    }
  }
}
</script>
<style>
.ant-upload.ant-upload-drag .ant-upload{
  padding: 0 !important;
}
</style>
