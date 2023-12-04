<template>
  <div class="carousel-container">
    <a-carousel autoplay effect="fade">
      <div v-for="(slide,index) in slides" :key="index">
        <div class="slide" :style="{ backgroundImage: `url(${slide.image})` }"></div>
      </div>
    </a-carousel>
  </div>
</template>

<script>
import { PageView } from '../layouts'

export default {
  name: 'Home',
  components: {
    PageView
  },
  data () {
    return {
      slides: [
        { id: 1, image: '/background.jpg' },
        { id: 2, image: '/background.jpg' },
        { id: 3, image: '/background.jpg' }
      ]
    }
  }
}
</script>
<style scoped>
.carousel-container {
  width: 100%;
  height: 100%;
}

.ant-carousel >>> .slick-slide {
  text-align: center;
  height: 100%;
  line-height: 160px;
  background: #364d79;
  overflow: hidden;
}
</style>
