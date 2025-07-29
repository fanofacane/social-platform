<script setup>
import { ref, onMounted } from 'vue';
// 周几数据
const weeks = ['周一', '周二', '周三', '周四', '周五', '周六', '周日'];
const currentWeek = ref(7); // 默认显示周日

// 番剧数据（已归类）
const animeWeekData = {
  '1': [ // 周一
    {
      id: 3234,
      title: '碧蓝之海 第二季',
      cover: 'https://lain.bgm.tv/pic/cover/l/d6/29/515880_96Oxr.jpg',
      score: '7.9',
      status: '预计7.7开播'
    },
    {
      id: 3232,
      title: '明天，美食广场见。',
      cover: 'https://lain.bgm.tv/pic/cover/l/0d/5b/528438_N77sG.jpg',
      score: '6.6',
      status: '预计7.7开播'
    },
    {
      id: 3156,
      title: '鬼人幻灯抄',
      cover: 'https://lain.bgm.tv/pic/cover/l/b8/b8/404753_v9Wri.jpg',
      score: '6.0',
      status: '暂停更新|每周一23:10'
    },
    {
      id: 3155,
      title: '夏日口袋',
      cover: 'https://lain.bgm.tv/pic/cover/l/23/ce/363957_pgptl.jpg',
      score: '7.3',
      status: '13|周一23:10'
    }
  ],
  '2': [ // 周二（新增：租借女友 第四季）
    {
      id: 3239,
      title: '最近的侦探真没用',
      cover: 'https://lain.bgm.tv/pic/cover/l/24/61/509216_2s20b.jpg',
      score: '6.4',
      status: '01|周二22:40'
    },
    {
      id: 3238,
      title: '死灵之子的宇宙恐怖秀',
      cover: 'https://lain.bgm.tv/pic/cover/l/bd/23/528147_g6KC7.jpg',
      score: '4.8',
      status: '01|时间待定'
    },
    {
      id: 3237,
      title: '杜鹃的婚约 第二季',
      cover: 'https://lain.bgm.tv/pic/cover/l/46/bc/505303_Za1G6.jpg',
      score: '5.6',
      status: '预计7.8开播'
    },
    {
      id: 3236,
      title: '活死喵之夜',
      cover: 'https://lain.bgm.tv/pic/cover/l/3b/e5/481745_O6iKr.jpg',
      score: '7.5',
      status: '预计7.8开播'
    },
    {
      id: 3235,
      title: '我怎么可能成为你的恋人，不行不行！(※不是不可能！？)',
      cover: 'https://lain.bgm.tv/pic/cover/l/ae/03/524707_1quxk.jpg',
      score: '7.5',
      status: '预计7.8开播'
    },
    // 新增：租借女友 第四季（周二）
    {
      id: 3265,
      title: '租借女友 第四季',
      cover: 'https://lain.bgm.tv/pic/cover/l/ae/03/524707_1quxk.jpg', // 未提供封面图，使用占位图
      score: '', // 未提供评分
      status: '01|周二22:00'
    }
  ],
  '3': [ // 周三
    {
      id: 3248,
      title: '新 吊带袜天使',
      cover: 'https://lain.bgm.tv/pic/cover/l/52/88/390555_11QS3.jpg',
      score: '7.9',
      status: '即将开播'
    }
  ],
  '4': [ // 周四
    {
      id: 3233,
      title: '坂本日常 第2部分',
      cover: 'https://lain.bgm.tv/pic/cover/l/04/95/524005_lSNqT.jpg',
      score: '6.4',
      status: '预计7.14开播'
    }
  ],
  '5': [ // 周五
    {
      id: 9998,
      title: '周五示例番剧1',
      cover: '',
      score: '7.2',
      status: '更新中|每周五19:00'
    },
    {
      id: 9997,
      title: '周五示例番剧2',
      cover: '',
      score: '8.1',
      status: '预计7.10开播'
    }
  ],
  '6': [ // 周六（新增4部番剧）
    // 新增：衣衫褴褛的大小姐受到姐姐前未婚夫的宠爱（周六）
    {
      id: 3264,
      title: '衣衫褴褛的大小姐受到姐姐前未婚夫的宠爱',
      cover: 'https://lain.bgm.tv/pic/cover/l/be/45/505782_PZQHP.jpg',
      score: '5.0',
      status: '01|周六上午'
    },
    // 新增：渡君的XX即将崩坏（周六）
    {
      id: 3263,
      title: '渡君的XX即将崩坏',
      cover: 'https://lain.bgm.tv/pic/cover/l/f8/a8/455454_cEllE.jpg',
      score: '5.3',
      status: '01|周六上午'
    },
    // 新增：安妮·雪莉（周六）
    {
      id: 3178,
      title: '安妮·雪莉',
      cover: 'https://lain.bgm.tv/pic/cover/l/2c/d5/524801_p2OVT.jpg',
      score: '7.0',
      status: '14|周六21:45'
    },
    // 新增：真･武士传 剑勇传说（周六）
    {
      id: 3177,
      title: '真･武士传 剑勇传说',
      cover: 'https://lain.bgm.tv/pic/cover/l/a4/1d/492881_5zfHH.jpg',
      score: '7.3',
      status: '13|每周六18:10'
    },
    // 新增：亚托莉 -我挚爱的时光-（周六，已完结）
    {
      id: 2620,
      title: '亚托莉 -我挚爱的时光-',
      cover: 'https://lain.bgm.tv/pic/cover/l/66/6d/397604_TgJ63.jpg',
      score: '6.3',
      status: '已完结'
    }
  ],
  '7': [ // 周日（新增9部番剧，预计7.6开播）
    // 新增：HOTEL INHUMANS（周日）
    {
      id: 3285,
      title: 'HOTEL INHUMANS',
      cover: 'https://lain.bgm.tv/pic/cover/l/e7/c7/523426_Jtmze.jpg',
      score: '7.5',
      status: '预计7.6开播'
    },
    // 新增：小城日常（周日）
    {
      id: 3284,
      title: '小城日常',
      cover: 'https://lain.bgm.tv/pic/cover/l/b2/c2/514358_oRoZH.jpg',
      score: '7.7',
      status: '预计7.6开播'
    },
    // 新增：废渊战鬼（周日）
    {
      id: 3283,
      title: '废渊战鬼',
      cover: 'https://lain.bgm.tv/pic/cover/l/ea/10/498947_FHYeb.jpg',
      score: '5.5',
      status: '预计7.6开播'
    },
    // 新增：与游戏中心的少女异文化交流的故事（周日）
    {
      id: 3282,
      title: '与游戏中心的少女异文化交流的故事',
      cover: 'https://lain.bgm.tv/pic/cover/l/55/d5/510256_09w10.jpg',
      score: '7.2',
      status: '预计7.6开播'
    },
    // 新增：肥宅勇者（周日）
    {
      id: 3281,
      title: '肥宅勇者',
      cover: 'https://lain.bgm.tv/pic/cover/l/69/01/524593_yyLxl.jpg',
      score: '6.5',
      status: '预计7.6开播'
    },
    // 新增：异世界默示录米诺戈拉（周日）
    {
      id: 3280,
      title: '异世界默示录米诺戈拉 ～从灭亡的文明开始的世界征服～',
      cover: 'https://lain.bgm.tv/pic/cover/l/f5/b1/500825_i02ef.jpg',
      score: '7.2',
      status: '预计7.6开播'
    },
    // 新增：禁止出入的鼹鼠（周日）
    {
      id: 3279,
      title: '禁止出入的鼹鼠',
      cover: 'https://lain.bgm.tv/pic/cover/l/bd/36/524289_41R9r.jpg',
      score: '8.0',
      status: '预计7.6开播'
    },
    // 新增：琉璃的宝石（周日）
    {
      id: 3278,
      title: '琉璃的宝石',
      cover: 'https://lain.bgm.tv/pic/cover/l/27/d7/512190_3FtCT.jpg',
      score: '7.3',
      status: '预计7.6开播'
    },
    // 新增：噗妮露是可爱史莱姆 第二季（周日）
    {
      id: 3277,
      title: '噗妮露是可爱史莱姆 第二季',
      cover: 'https://lain.bgm.tv/pic/cover/l/14/f9/530115_8prir.jpg',
      score: '8.1',
      status: '预计7.6开播'
    }
  ]
};

// 当前显示的番剧列表
const currentAnimeList = ref([]);
// 切换周次
const switchWeek = (weekIndex) => {
  currentWeek.value = weekIndex;
  currentAnimeList.value = animeWeekData[weekIndex] || [];
  // 添加切换动画
  const listContainer = document.querySelector('.anime-list-container');
  if (listContainer) {
    listContainer.classList.add('fade-out');
    setTimeout(() => {
      listContainer.classList.remove('fade-out');
    }, 0);
  }
};

// 处理图片加载错误
const handleImageError = (index) => {
  currentAnimeList.value[index].cover = placeholderImage;
};



// 组件挂载时初始化
onMounted(() => {
  currentAnimeList.value = animeWeekData[currentWeek.value] || [];
  newAnimeList.value = animeData;
});
// 响应式数据
const sectionTitle = ref('热乎の新番');
const newAnimeList = ref([]);
const showRating = ref(true);

// 新番数据
const animeData = [
  {
    id: 3269,
    title: '薰香花朵凛然绽放',
    href: '/bangumi/3269.html',
    cover: 'https://lain.bgm.tv/pic/cover/l/b8/0d/513345_jv4wM.jpg',
    rating: '7.4'
  },
  {
    id: 3274,
    title: '和雨和你',
    href: '/bangumi/3274.html',
    cover: 'https://lain.bgm.tv/pic/cover/l/75/94/509062_mC4D1.jpg',
    rating: '7.0'
  },
  {
    id: 3270,
    title: '光死去的夏天',
    href: '/bangumi/3270.html',
    cover: 'https://lain.bgm.tv/pic/cover/l/c6/a6/496086_2tPPi.jpg',
    rating: '7.2'
  },
  {
    id: 3275,
    title: '公爵千金的家庭教师',
    href: '/bangumi/3275.html',
    cover: 'https://lain.bgm.tv/pic/cover/l/ac/67/459616_sbtv1.jpg',
    rating: '6.2'
  },
  {
    id: 3266,
    title: '9-nine- 支配者的王冠',
    href: '/bangumi/3266.html',
    cover: 'https://lain.bgm.tv/pic/cover/l/0a/ad/496973_kUlKl.jpg',
    rating: '5.1'
  },
  {
    id: 3273,
    title: '更衣人偶坠入爱河 第二季',
    href: '/bangumi/3273.html',
    cover: 'https://lain.bgm.tv/pic/cover/l/38/44/398951_M97dM.jpg',
    rating: '7.2'
  },
  {
    id: 3271,
    title: '不良少女',
    href: '/bangumi/3271.html',
    cover: 'https://lain.bgm.tv/pic/cover/l/df/1b/501614_SzXy2.jpg',
    rating: '6.6'
  },
  {
    id: 3272,
    title: '青春笨蛋少年不做圣诞服女郎的梦',
    href: '/bangumi/3272.html',
    cover: 'https://lain.bgm.tv/pic/cover/l/d3/a3/467930_jH4s4.jpg',
    rating: '7.3'
  },
  {
    id: 3259,
    title: '气绝勇者与暗杀公主',
    href: '/bangumi/3259.html',
    cover: 'https://lain.bgm.tv/pic/cover/l/8e/92/536363_42Ul2.jpg',
    rating: '6.1'
  },
  {
    id: 3267,
    title: '费马的料理',
    href: '/bangumi/3267.html',
    cover: 'https://lain.bgm.tv/pic/cover/l/5f/98/531945_ptCCC.jpg',
    rating: '5.7'
  },
  {
    id: 3268,
    title: '怪兽8号 第二季',
    href: '/bangumi/3268.html',
    cover: 'https://lain.bgm.tv/pic/cover/l/19/39/501844_Lh308.jpg',
    rating: '6.4'
  },
  {
    id: 3255,
    title: '被驱逐开勇者派对的白魔导师，被S级冒险者捡到 ～这个白魔导师超规格～',
    href: '/bangumi/3255.html',
    cover: 'https://lain.bgm.tv/pic/cover/l/19/38/505793_HF3Ug.jpg',
    rating: '5.3'
  },
  {
    id: 3178,
    title: '安妮·雪莉',
    href: '/bangumi/3178.html',
    cover: 'https://lain.bgm.tv/pic/cover/l/2c/d5/524801_p2OVT.jpg',
    rating: '7.0'
  },
  {
    id: 3177,
    title: '真･武士传 剑勇传说',
    href: '/bangumi/3177.html',
    cover: 'https://lain.bgm.tv/pic/cover/l/a4/1d/492881_5zfHH.jpg',
    rating: '7.3'
  },
  {
    id: 3263,
    title: '渡君的XX即将崩坏',
    href: '/bangumi/3263.html',
    cover: 'https://lain.bgm.tv/pic/cover/l/f8/a8/455454_cEllE.jpg',
    rating: '5.3'
  }
];

const cardImages = ref([
  {
    id: 1,
    name: 'iPhone 13',
    description: '最新款苹果手机',
    imageUrl: 'https://play.xfvod.pro/images/hb/gyro.png',
  },
  {
    id: 2,
    name: 'MacBook Pro',
    description: '高性能笔记本电脑',
    imageUrl: 'https://play.xfvod.pro/images/hb/cyzgs2.jpg',
  },
  {
    id: 3,
    name: 'AirPods Pro',
    description: '主动降噪耳机',
    imageUrl: 'https://play.xfvod.pro/images/hb/zhangyu.png',
  }
]);
</script>

<template>
  <el-carousel :interval="4000" type="card" height="350px">
    <el-carousel-item v-for="item in cardImages" :key="item">
        <div class="carousel-content">
          <img :src="item.imageUrl" :alt="item.name" class="carousel-image">
        </div>
    </el-carousel-item>
  </el-carousel>
<div class="app-container">
    <!-- 标题和周选择区域 -->
    <div class="title-container">
      <h4 class="title">追番周表</h4>
      <div class="week-selector">
        <div 
          v-for="(week, index) in weeks" 
          :key="index" 
          :class="['week-btn', currentWeek === index + 1 ? 'active' : '']"
          @click="switchWeek(index + 1)"
        >
          {{ week }}
        </div>
      </div>
    </div>
    
    <!-- 番剧列表区域 - 横向展示 -->
    <div class="anime-list-container">
      <div v-if="currentAnimeList.length > 0" class="anime-list horizontal">
        <div 
          v-for="(anime, index) in currentAnimeList" 
          :key="index" 
          class="anime-card"
          @click="goToAnimeDetail(anime.id)"
        >
          <div class="anime-image-container">
            <img 
              :src="anime.cover || placeholderImage" 
              :alt="anime.title" 
              class="anime-image"
              @error="handleImageError(index)"
            >
            <div class="image-overlay">
              <span class="score" v-if="anime.score">{{ anime.score }}</span>
            </div>
          </div>
          <div class="anime-info">
            <h5 class="anime-title">{{ anime.title }}</h5>
            <p class="anime-subtitle">{{ anime.status }}</p>
          </div>
        </div>
      </div>
      
      <!-- 无数据提示 -->
      <div v-else class="no-data-tip">暂无该日番剧信息</div>
    </div>
  </div>
  <div class="overflow">
    <div class="title flex between top40">
      <div class="title-left">
        <h4 class="title-h cor4">{{ sectionTitle }}</h4>
      </div>
    </div>
    <div class="animefx">
      <div 
        v-for="item in newAnimeList" 
        :key="item.id" 
        class="animebx"
      >
        <div class="public-list-div public-list-bj">
          <a 
            :target="_blank" 
            class="public-list-exp" 
            :title="item.title"
          >
            <img 
              :src="item.cover" 
              :alt="item.title" 
              class="gen-movie-img"
              referrerpolicy="no-referrer"
            >
            <span class="public-bg"></span>
            <span v-if="showRating" class="public-list-prb"><i class="ft4">{{ item.rating }}</i></span>
            <span class="public-play"><i class="fa"></i></span>
          </a>
        </div>
        <div class="public-list-button">
          <a 
            :target="_blank" 
            class="time-title ft4 bold" 
            :href="item.href" 
            :title="item.title"
          >{{ item.title }}</a>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.el-carousel__item h3 {
  color: #0f59ba;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
  text-align: center;
}
.el-carousel__item img{
    width: 100%;
    height: 100%;
    object-fit: contain; /* 图片覆盖容器，可能裁剪 */
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}
.app-container {
  max-width: 100%;
  margin: 0;
  padding: 20px;
}

.title-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding: 0 20px;
}

.title {
  font-size: 1.8rem;
  font-weight: bold;
  color: #333;
  margin: 0;
}

.week-selector {
  display: flex;
  gap: 10px;
}

.week-btn {
  padding: 8px 16px;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
  background-color: #fff;
  color: #666;
  font-weight: 500;
  transition: all 0.3s ease;
  white-space: nowrap;
}

.week-btn.active {
  background-color: #4a90e2;
  color: white;
  border-color: #4a90e2;
}

.anime-list-container {
  overflow-x: auto;
  padding-bottom: 20px;
  position: relative;
}

.anime-list {
  display: flex;
  gap: 20px;
  padding: 0 20px;
  min-width: max-content;
}

.anime-list.horizontal {
  flex-direction: row;
}

.anime-card {
  min-width: 250px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  cursor: pointer;
  flex-shrink: 0;
}

.anime-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.15);
}

.anime-image-container {
  position: relative;
  height: 180px;
  overflow: hidden;
}

.anime-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.anime-card:hover .anime-image {
  transform: scale(1.05);
}

.image-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(to bottom, rgba(0,0,0,0) 0%, rgba(0,0,0,0.7) 100%);
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  padding: 10px;
  box-sizing: border-box;
}

.score {
  background-color: rgba(255, 255, 255, 0.8);
  color: #333;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 0.8rem;
}

.play-icon {
  background-color: rgba(255, 255, 255, 0.8);
  color: #e74c3c;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 1rem;
}

.anime-info {
  padding: 12px;
  background-color: #fff;
}

.anime-title {
  font-size: 1rem;
  font-weight: bold;
  margin-bottom: 6px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.anime-subtitle {
  font-size: 0.8rem;
  color: #666;
  margin: 0;
}

.no-data-tip {
  text-align: center;
  padding: 40px 0;
  color: #999;
  font-size: 1.1rem;
  background-color: #f9f9f9;
  border-radius: 8px;
}

.fade-out {
  opacity: 0;
  transition: opacity 0.3s ease;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .anime-card {
    min-width: 200px;
  }
  
  .title {
    font-size: 1.5rem;
  }
}

@media (max-width: 480px) {
  .anime-card {
    min-width: 180px;
  }
  
  .week-btn {
    padding: 8px 12px;
    font-size: 0.9rem;
  }
}
/* 容器基础样式 */
.overflow {
  width: 100%;
  box-sizing: border-box;
}

.animefx {
  /* 使用CSS Grid实现网格布局 */
  display: grid;
  /* 每行显示6个卡片，100%/6=16.666% */
  grid-template-columns: repeat(auto-fill, minmax(16.666%, 1fr));
  /* 网格间距 */
  gap: 15px;
  margin: 0;
  padding: 0;
}

/* 动漫卡片样式 */
.animebx {
  width: 100%;
  box-sizing: border-box;
  overflow: hidden;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

/* 卡片悬停效果 */
.animebx:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.15);
}

/* 图片容器样式 */
.public-list-div {
  position: relative;
  padding-bottom: 140%; /* 保持16:9宽高比 */
  overflow: hidden;
}

/* 图片样式 */
.gen-movie-img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

/* 卡片悬停时图片放大效果 */
.animebx:hover .gen-movie-img {
  transform: scale(1.05);
}

/* 背景遮罩样式 */
.public-bg {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 40%;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.8), transparent);
  z-index: 1;
}

/* 评分和播放按钮样式 */
.public-list-prb,
.public-play {
  position: absolute;
  z-index: 2;
  color: white;
  background-color: rgba(0, 0, 0, 0.6);
  padding: 3px 8px;
  border-radius: 20px;
  font-size: 12px;
}

.public-list-prb {
  top: 10px;
  left: 10px;
}

.public-play {
  bottom: 10px;
  right: 10px;
}

/* 标题样式 */
.public-list-button {
  padding: 10px 0;
  text-align: center;
}

.time-title {
  display: block;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  color: #333;
  font-size: 14px;
  text-decoration: none;
  font-weight: bold;
}

/* 响应式设计 - 中等屏幕 */
@media (max-width: 1200px) {
  .animefx {
    grid-template-columns: repeat(auto-fill, minmax(20%, 1fr)); /* 5列 */
  }
}

/* 响应式设计 - 小屏幕 */
@media (max-width: 992px) {
  .animefx {
    grid-template-columns: repeat(auto-fill, minmax(25%, 1fr)); /* 4列 */
  }
}

/* 响应式设计 - 平板 */
@media (max-width: 768px) {
  .animefx {
    grid-template-columns: repeat(auto-fill, minmax(33.333%, 1fr)); /* 3列 */
  }
}

/* 响应式设计 - 手机 */
@media (max-width: 576px) {
  .animefx {
    grid-template-columns: repeat(auto-fill, minmax(50%, 1fr)); /* 2列 */
  }
}
</style>

