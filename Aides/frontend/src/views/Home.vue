<template>
  <div class="home">
    <h1>AI议会系统</h1>
    <textarea v-model="question" placeholder="请输入您的问题"></textarea>
    <button @click="submitQuestion">提交</button>
    <div v-if="result">
      <h3>最终答复：</h3>
      <p>{{ result }}</p>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'Home',
  data() {
    return {
      question: '',
      result: ''
    }
  },
  methods: {
    async submitQuestion() {
      try {
        const response = await axios.post('http://localhost:8080/api/ask', {
          question: this.question
        })
        this.result = response.data.answer
      } catch (error) {
        console.error('Error:', error)
        this.result = '请求失败，请重试'
      }
    }
  }
}
</script>

<style scoped>
.home {
  max-width: 800px;
  margin: 0 auto;
}
textarea {
  width: 100%;
  height: 100px;
  margin: 20px 0;
}
button {
  padding: 10px 20px;
  background: #42b983;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
</style>