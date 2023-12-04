<template>
  <div class="card-list" ref="content">

    <a-card style="margin-bottom: 16px" title="商品管理">
      <a slot="extra" href="#">
        <a-button size="large" @click="handleCreate" type="primary">新增</a-button>
      </a>
    </a-card>

    <a-list
      :grid="{gutter: 24, lg: 3, md: 2, sm: 1, xs: 1}"
      :dataSource="dataSource"
    >
      <a-list-item slot="renderItem" slot-scope="item">
        <a-card :hoverable="true">
          <a-card-meta>
            <div style="margin-bottom: 3px" slot="title">{{ item.giftName }}</div>
            <a-avatar
              :size="122"
              shape="square"
              class="card-avatar"
              slot="avatar"
              :src="item.giftAvatar"/>
            <div class="meta-content" slot="description">{{ item.giftDescription }}</div>
          </a-card-meta>
          <template slot="actions">
            <span>{{ item.createTime.match(/^\d{4}-\d{2}-\d{2}/)[0] }}</span>
            <span>{{ item.giftPrice }}积分</span>
            <a-icon @click="handleUpdate(item)" type="edit"/>
            <a-popconfirm title="确认删除？" ok-text="是" cancel-text="否" @confirm="handleDel(item.giftId)">
              <a-icon type="delete"/>
            </a-popconfirm>
          </template>
        </a-card>
      </a-list-item>
    </a-list>
    <a-modal :visible="cuModalVisible" :title="isCreate?'新增商品':'编辑商品'" @cancel="handleCancel">
      <a-spin :spinning="confirmLoading">
        <a-form v-bind="formItemLayout" :form="form">
          <a-form-item label="物品封面">
            <div class="dropbox">
              <a-upload-dragger
                name="avatar"
                accept=".jpg, .jpeg, .png"
                class="avatar-uploader"
                v-decorator="[
                  'giftAvatar',
                  {
                    initialValue: formItem.giftAvatar,
                    getValueFromEvent: normFile,
                    rules: [{ required: true, message: '请上传图片' }]
                  },
                ]"
                :show-upload-list="false"
                :before-upload="beforeUpload"
                :customRequest="customRequest"
                :multiple="false"
                @change="handleChange"
              >
                <img style="width: 100%;" v-if="formItem.giftAvatar" :src="formItem.giftAvatar" alt="avatar"/>
                <div style="margin: 55px;" v-else>
                  <a-icon :type="loading ? 'loading' : 'plus'"/>
                  <div class="ant-upload-text">
                    拖拽图片至此处或点击上传
                  </div>
                  <p class="ant-upload-hint">
                    支持JPEG、PNG格式图片
                  </p>
                </div>
              </a-upload-dragger>
            </div>
          </a-form-item>
          <a-form-item v-show="false" label="物品ID">
            <a-input
              v-decorator="['giftId', { initialValue: formItem.giftId}]"/>
          </a-form-item>
          <a-form-item label="物品名称">
            <a-input
              placeholder="请输入物品名称"
              v-decorator="['giftName', {initialValue: formItem.giftName,rules: [{ required: true, message: '请输入物品名称' }]}]"/>
          </a-form-item>
          <a-form-item label="物品描述">
            <a-input
              placeholder="请输入物品描述"
              v-decorator="['giftDescription', {initialValue: formItem.giftDescription,rules: [{ required: true, message: '请输入物品描述' }]}]"/>
          </a-form-item>
          <a-form-item label="物品价格">
            <a-input-number
              :min="0"
              placeholder="请输入物品价格"
              v-decorator="['giftPrice', {initialValue:formItem.giftPrice }]"/>
          </a-form-item>
        </a-form>
      </a-spin>
      <template slot="footer">
        <a-button key="cancel" @click="handleCancel">取消</a-button>
        <a-button key="forward" type="primary" @click="handleConfirm()">确认</a-button>
      </template>
    </a-modal>
  </div>
</template>

<script>
import { createGift, updateGift, delGift, getGiftList } from '../../api/gift'
import { postprocess, uploadImage } from '../../api/file'
function getBase64 (img, callback) {
  const reader = new FileReader()
  reader.addEventListener('load', () => callback(reader.result))
  reader.readAsDataURL(img)
}
export default {
  name: 'ShopList',
  data () {
    return {
      formItemLayout: {
        labelCol: {
          xs: { span: 6 },
          sm: { span: 6 }
        },
        wrapperCol: {
          xs: { span: 18 },
          sm: { span: 18 }
        }
      },
      dataSource: [],
      cuModalVisible: false,
      isCreate: true,
      formItem: {
        giftId: null,
        giftName: null,
        giftPrice: 0,
        giftDescription: null,
        giftAvatar: null
      },
      form: this.$form.createForm(this),
      loading: false,
      imageUrl: '',
      confirmLoading: false
    }
  },
  methods: {
    handleCreate () {
      this.isCreate = true
      this.cuModalVisible = true
      this.form.resetFields()
      this.formItem = {
        giftId: null,
        giftName: null,
        giftPrice: 0,
        giftDescription: null,
        giftAvatar: null
      }
    },
    handleUpdate (gift) {
      this.isCreate = false
      this.cuModalVisible = true
      this.formItem = { ...gift }
      this.form.setFieldsValue(this.formItem)
    },
    handleDel (giftId) {
      delGift(giftId).then(res => {
        if (res.code === 0) {
          this.loadGiftList()
        } else {
          this.$notification.error({
            message: '删除失败',
            description: res.msg
          })
        }
      })
    },
    normFile (e) {
      return e.file.originFileObj
    },
    handleConfirm () {
      console.log(this.form.getFieldsValue())

      this.form.validateFields(async (errors, values) => {
        if (!errors) {
          this.confirmLoading = true
          if (typeof values.giftAvatar !== 'string') {
            const resp = await uploadImage(values.giftAvatar)
            values.giftAvatar = postprocess(resp)
          }
          if (this.isCreate) {
            createGift(values).then(res => {
              if (res.code === 0) {
                this.$notification.success({
                  message: '创建成功'
                })
                this.cuModalVisible = false
                this.loadGiftList()
              } else {
                this.$notification.error({
                  message: '创建失败',
                  description: res.msg
                })
              }
            }).finally(() => {
              this.confirmLoading = false
            })
          } else {
            updateGift(values).then(res => {
              if (res.code === 0) {
                this.$notification.success({
                  message: '更新成功'
                })
                this.cuModalVisible = false
                this.loadGiftList()
              } else {
                this.$notification.error({
                  message: '更新失败',
                  description: res.msg
                })
              }
            }).finally(() => {
              this.confirmLoading = false
            })
          }
        }
      })
    },
    loadGiftList () {
      getGiftList().then(res => {
        if (res.code === 0) {
          this.dataSource = res.data
        } else {
          this.$notification.error({
            message: '获取物品列表失败',
            description: res.msg
          })
        }
      })
    },
    handleCancel () {
      this.cuModalVisible = false
    },
    handleChange (info) {
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
      getBase64(file, imageUrl => {
        this.formItem.giftAvatar = imageUrl
        this.loading = false
      })
    }
  },
  mounted () {
    this.loadGiftList()
  }
}
</script>

<style lang="less" scoped>
.card-avatar {
  border-color: #e8e8e8;
  border-width: 2px;
  padding: 5px;
  border-style: solid;
}

.meta-content {
  position: relative;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  height: 64px;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}
</style>
<style>
.ant-upload.ant-upload-drag .ant-upload{
  padding: 0 !important;
}
</style>
