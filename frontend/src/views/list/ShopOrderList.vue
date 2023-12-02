<template>
  <a-card style="margin-top: 24px" :bordered="false" title="历史订单">
    <!--      <a-list size="large" :data-source="data">-->
    <!--        <a-list-item slot="renderItem" slot-scope="item, index">-->
    <!--          <a-list-item-meta :description="item.giftDescription">-->
    <!--            <a-avatar slot="avatar" size="large" shape="square" :src="item.giftAvatar"/>-->
    <!--            <a slot="title">{{ item.giftName }}</a>-->
    <!--          </a-list-item-meta>-->
    <!--          &lt;!&ndash;          <div slot="actions">&ndash;&gt;-->
    <!--          &lt;!&ndash;                      <a @click="viewExamRecordDetail(item.examRecord)">查看考试详情</a>&ndash;&gt;-->
    <!--          &lt;!&ndash;          </div>&ndash;&gt;-->
    <!--          <div class="list-content">-->
    <!--            <div class="list-content-item">-->
    <!--              <span>联系人</span>-->
    <!--              <p>{{ item.orderContact }}</p>-->
    <!--            </div>-->
    <!--            <div class="list-content-item">-->
    <!--              <span>联系电话</span>-->
    <!--              <p>{{ item.orderPhone }}</p>-->
    <!--            </div>-->
    <!--            <div class="list-content-item">-->
    <!--              <span>收货地址</span>-->
    <!--              <p>{{ item.orderAddress }}</p>-->
    <!--            </div>-->
    <!--            <div class="list-content-item">-->
    <!--              <span>消耗积分</span>-->
    <!--              <p>{{ item.giftPrice }}</p>-->
    <!--            </div>-->
    <!--            <div class="list-content-item">-->
    <!--              <span>购买时间</span>-->
    <!--              <p>{{ item.createTime.split(' ')[0] }}</p>-->
    <!--            </div>-->
    <!--          </div>-->
    <!--        </a-list-item>-->
    <!--      </a-list>-->
    <a-table
      bordered
      size="middle"
      :pagination="false"
      :columns="columns"
      :data-source="data">
      <span slot="giftAvatar" slot-scope="text, record" >
        <img style="width: 66px;" :src="text">
      </span>
    </a-table>
    <a-pagination
      style="margin-top: 10px"
      v-model="page"
      :page-size-options="pageSizeOptions"
      :total="totalElements"
      show-quick-jumper
      show-size-changer
      :page-size="size"
      @change="loadList"
      @showSizeChange="handleSizeChange"
    />
  </a-card>
</template>

<script>
import { getGiftOrderList } from '../../api/gift'

const columns = [
  {
    title: '订单号',
    dataIndex: 'giftOrderId',
    width: '100px'
  },
  {
    title: '封面',
    dataIndex: 'giftAvatar',
    align: 'center',
    scopedSlots: { customRender: 'giftAvatar' }
  }, {
    title: '物品名',
    dataIndex: 'giftName'
  },
  // {
  //   title: '物品描述',
  //   dataIndex: 'giftDescription'
  // },
  {
    title: '物品价格',
    dataIndex: 'giftPrice'
  },
  {
    title: '联系人',
    dataIndex: 'orderContact'
  },
  {
    title: '联系电话',
    dataIndex: 'orderPhone'
  },
  {
    title: '收货地址',
    dataIndex: 'orderAddress'
  },
  {
    title: '创建时间',
    dataIndex: 'createTime'
  }
]
export default {
  name: 'ShopOrderList',
  data () {
    return {
      pageSizeOptions: ['10', '20', '30', '40', '50'],
      page: 1,
      size: 10,
      totalElements: 0,
      data: [],
      columns
    }
  },
  methods: {
    loadList () {
      getGiftOrderList({ page: this.page - 1, size: this.size }).then(res => {
        if (res.code === 0) {
          this.data = res.data.content
          this.totalElements = res.data.totalElements
        } else {
          this.$notification.error({
            message: '获取订单记录失败',
            description: res.msg
          })
        }
      })
    },
    handleSizeChange (current, size) {
      this.size = size
      this.page = 1
      this.loadList()
    }
  },
  mounted () {
    this.loadList()
  }
}
</script>

<style lang="less" scoped>
.ant-avatar-lg {
  width: 48px;
  height: 48px;
  line-height: 48px;
}

.list-content-item {
  color: rgb(0, 0, 0);
  display: inline-block;
  vertical-align: middle;
  font-size: 14px;
  margin-left: 14px;
  text-align: center;

  span {
    color: rgba(0, 0, 0, .45);
    line-height: 20px;
  }

  p {
    margin-top: 4px;
    margin-bottom: 0;
    line-height: 22px;
  }
}
</style>
