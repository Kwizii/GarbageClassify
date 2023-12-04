<template>
  <div class="carousel-container">
    <a-carousel autoplay effect="fade">
      <div v-for="(slide,index) in slides" :key="index">
        <div class="slide" :style="{backgroundImage:`url(${slide.image})`}">
        </div>
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
        {image: '/1.png'},
        {image: '/2.png'},
        {image: '/3.png'},
        {image: '/4.png'}
      ]
    }
  }
}
</script>
<style scoped>

.slide {
  width: 100%;
  height: 80vh;
  background-color: #8cc8ff;
  background-position: center;
  background-repeat: no-repeat;
  background-size: cover;
}
</style>
<style>
.ant-carousel .slick-dots li {
  background-color: #8cc8ff;
}

.ant-carousel .slick-dots .slick-active button {
  background-color: #2f54eb !important;
}
</style>
