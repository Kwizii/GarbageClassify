<template>
  <div class="card-list" ref="content">
    <a-list
      :grid="{gutter: 24, lg: 3, md: 2, sm: 1, xs: 1}"
      :dataSource="dataSource"
    >
      <a-list-item slot="renderItem" slot-scope="item">
        <a-card :hoverable="true" @click="handleClick(item)">
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
            <span>上架时间：{{ item.createTime.match(/^\d{4}-\d{2}-\d{2}/)[0] }}</span>
            <span>价格：{{ item.giftPrice }}积分</span>
          </template>
        </a-card>
      </a-list-item>
    </a-list>
    <a-modal :visible="confirmVisible" title="礼物兑换" :confirm-loading="confirmLoading" @cancel="handleCancel">
      <a-spin :spinning="confirmLoading">
        <a-steps :current="curStep" style="margin-bottom: 10px">
          <a-step title="确认">
            <a-icon slot="icon" type="question"/>
          </a-step>
          <a-step title="填写信息">
            <a-icon slot="icon" type="solution"/>
          </a-step>
        </a-steps>
        <p v-show="curStep===0">你当前拥有<span style="color: #4c9ff4">{{ user.state.info.score }}</span>积分，你确认要使用<span
          style="color: #4c9ff4">{{ curItem.giftPrice }}</span>积分兑换吗?</p>
        <a-form v-show="curStep>=1" :form="form">
          <a-form-item label="联系人" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input
              placeholder="请输入联系人名称"
              v-decorator="['orderContact', {rules: [{ required: true, message: '请输入联系人名称' }]}]"/>
          </a-form-item>
          <a-form-item label="联系号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input
              placeholder="请输入11 位手机号"
              v-decorator="['orderPhone', {rules: [{ required: true, message: '请输入正确的手机号', pattern: /^1[3456789]\d{9}$/}] }]">
              <a-select slot="addonBefore" size="large" defaultValue="+86">
                <a-select-option value="+86">+86</a-select-option>
              </a-select>
            </a-input>
          </a-form-item>
          <a-form-item label="收货地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input
              placeholder="请输入收货地址"
              v-decorator="['orderAddress', {rules: [{ required: true, message: '请输入收货地址' }]}]"/>
          </a-form-item>
        </a-form>
      </a-spin>
      <template slot="footer">
        <a-button key="cancel" @click="handleCancel">取消</a-button>
        <a-button key="forward" :loading="confirmLoading" type="primary" @click="handleNext()">
          {{ curStep === 1 && '兑换' || '确认' }}
        </a-button>
      </template>
    </a-modal>
  </div>
</template>

<script>
import { createGiftOrder, getGiftList } from '../../api/gift'
import user from '../../store/modules/user'

export default {
  name: 'ShopList',
  computed: {
    user () {
      return user
    }
  },
  data () {
    return {
      description: '积分兑换商品',
      labelCol: {
        xs: { span: 6 },
        sm: { span: 6 }
      },
      size: 'default',
      wrapperCol: {
        xs: { span: 18 },
        sm: { span: 18 }
      },
      extraImage: '/assets/gift.svg',
      dataSource: [],
      confirmVisible: false,
      curStep: 0,
      curItem: {
        giftPrice: 0
      },
      form: this.$form.createForm(this),
      confirmLoading: false
    }
  },
  methods: {
    handleClick (item) {
      this.confirmVisible = true
      this.curItem = item
    },
    handleNext () {
      this.curStep++
      if (this.curStep === 1) {
        if (user.state.info.score < this.curItem.giftPrice) {
          this.$notification.error({
            message: '兑换失败',
            description: '积分不足, 多去参与考试获得积分吧'
          })
          this.handleCancel()
        }
        return
      }
      const { form: { validateFields } } = this
      this.confirmLoading = true
      validateFields((errors, values) => {
        if (!errors) {
          values.giftId = this.curItem.giftId
          setTimeout(() => {
            createGiftOrder(values).then(res => {
              if (res.code === 0) {
                this.$notification.success({
                  message: '兑换成功'
                })
                user.state.info.score -= this.curItem.giftPrice
              } else {
                this.$notification.error({
                  message: '兑换失败',
                  description: res.msg
                })
              }
            }).finally(() => {
              this.confirmLoading = false
              this.handleCancel()
            })
          }, 888)
        } else {
          this.confirmLoading = false
        }
      })
    },
    handleCancel () {
      this.form.resetFields()
      this.confirmVisible = false
      this.curStep = 0
    }
  },
  mounted () {
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
