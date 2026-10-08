import { createApp, ref, onMounted } from 'vue'
import ElementPlus, { ElMessage, ElMessageBox } from 'element-plus'
import 'element-plus/dist/index.css'
import { ChatDotRound, Star, Delete, Picture } from '@element-plus/icons-vue'
import axios from 'axios'
import './style.css'

const api = axios.create({ baseURL: '/api', withCredentials: true })
api.interceptors.response.use(
  response => response,
  error => Promise.reject(new Error(error.response?.data?.message || '请求失败，请稍后重试'))
)

const App = {
  components: { ChatDotRound, Star, Delete, Picture },
  setup() {
    const user = ref(null)
    const posts = ref([])
    const authVisible = ref(false)
    const authMode = ref('login')
    const authForm = ref({ username: '', password: '' })
    const postForm = ref({ content: '', imageUrl: '' })
    const submitting = ref(false)

    const loadPosts = async () => { posts.value = (await api.get('/posts')).data }
    const loadUser = async () => { try { user.value = (await api.get('/auth/me')).data } catch { user.value = null } }
    const openAuth = (mode = 'login') => { authMode.value = mode; authForm.value = { username: '', password: '' }; authVisible.value = true }
    const authenticate = async () => {
      submitting.value = true
      try {
        user.value = (await api.post(`/auth/${authMode.value}`, authForm.value)).data
        authVisible.value = false
        ElMessage.success(authMode.value === 'login' ? '欢迎回来！' : '注册成功，欢迎加入！')
        await loadPosts()
      } catch (error) { ElMessage.error(error.message) } finally { submitting.value = false }
    }
    const logout = async () => { await api.post('/auth/logout'); user.value = null; await loadPosts(); ElMessage.success('已退出登录') }
    const publish = async () => {
      if (!user.value) return openAuth()
      if (!postForm.value.content.trim()) return ElMessage.warning('写点内容再发布吧')
      submitting.value = true
      try { await api.post('/posts', postForm.value); postForm.value = { content: '', imageUrl: '' }; ElMessage.success('动态已发布'); await loadPosts() } catch (error) { ElMessage.error(error.message) } finally { submitting.value = false }
    }
    const upload = response => { postForm.value.imageUrl = response.url; ElMessage.success('图片上传成功') }
    const beforeUpload = () => { if (!user.value) { openAuth(); return false } return true }
    const like = async post => { if (!user.value) return openAuth(); try { await api.post(`/posts/${post.id}/likes`); await loadPosts() } catch (error) { ElMessage.error(error.message) } }
    const comment = async post => {
      if (!user.value) return openAuth()
      try {
        const { value } = await ElMessageBox.prompt('留下你的想法', '评论动态', { inputPattern: /\S+/, inputErrorMessage: '评论不能为空' })
        await api.post(`/posts/${post.id}/comments`, { content: value }); await loadPosts()
      } catch (error) { if (error !== 'cancel' && error !== 'close') ElMessage.error(error.message) }
    }
    const remove = async post => {
      try { await ElMessageBox.confirm('删除后无法恢复，确定删除这条动态？', '删除动态', { type: 'warning' }); await api.delete(`/posts/${post.id}`); ElMessage.success('已删除'); await loadPosts() } catch (error) { if (error !== 'cancel' && error !== 'close') ElMessage.error(error.message) }
    }
    onMounted(async () => { await loadUser(); await loadPosts() })
    return { user, posts, authVisible, authMode, authForm, postForm, submitting, openAuth, authenticate, logout, publish, upload, beforeUpload, like, comment, remove }
  },
  template: `
    <div class="page">
      <header>
        <div class="brand">星屿 <span>SPACE</span></div>
        <div class="head-actions">
          <template v-if="user"><span class="welcome">你好，{{ user.username }}</span><el-button text @click="logout">退出</el-button></template>
          <template v-else><el-button text @click="openAuth('login')">登录</el-button><el-button type="primary" round @click="openAuth('register')">注册</el-button></template>
        </div>
      </header>
      <main>
        <section class="hero"><p class="eyebrow">记录生活里的闪光瞬间</p><h1>把日常，放进你的<br><em>小小宇宙</em></h1><p class="sub">一张照片，一段心情，和朋友们的温柔回应。</p></section>
        <section class="composer">
          <div class="composer-title"><el-icon><ChatDotRound /></el-icon> 今天有什么新鲜事？</div>
          <el-input v-model="postForm.content" type="textarea" :rows="3" maxlength="1000" show-word-limit placeholder="分享此刻的心情..." />
          <el-image v-if="postForm.imageUrl" class="preview" :src="postForm.imageUrl" fit="cover" />
          <div class="composer-bottom"><el-upload action="/api/uploads" :show-file-list="false" :on-success="upload" :before-upload="beforeUpload" name="file"><el-button text type="primary"><el-icon><Picture /></el-icon> 添加照片</el-button></el-upload><el-button type="primary" round :loading="submitting" @click="publish">发布动态</el-button></div>
        </section>
        <section class="feed"><h2>最新动态 <span>{{ posts.length }} 条</span></h2><el-empty v-if="!posts.length" description="还没有动态，来发布第一条吧" />
          <article v-for="post in posts" :key="post.id" class="post">
            <div class="avatar">{{ post.username.slice(0, 1).toUpperCase() }}</div>
            <div class="post-body"><div class="post-meta"><b>{{ post.username }}</b><time>{{ new Date(post.createdAt).toLocaleString() }}</time></div><p class="content">{{ post.content }}</p><el-image v-if="post.imageUrl" class="post-image" :src="post.imageUrl" fit="cover" :preview-src-list="[post.imageUrl]" />
              <div class="post-actions"><el-button text :type="post.liked ? 'danger' : ''" @click="like(post)"><el-icon><Star /></el-icon>{{ post.liked ? '已赞' : '点赞' }} {{ post.likeCount }}</el-button><el-button text @click="comment(post)"><el-icon><ChatDotRound /></el-icon>评论 {{ post.comments.length }}</el-button><el-button v-if="user?.id === post.userId" text type="danger" @click="remove(post)"><el-icon><Delete /></el-icon>删除</el-button></div>
              <div v-if="post.comments.length" class="comments"><div v-for="item in post.comments" :key="item.id"><b>{{ item.username }}</b>：{{ item.content }}</div></div>
            </div>
          </article>
        </section>
      </main>
      <el-dialog v-model="authVisible" width="390px" :title="authMode === 'login' ? '欢迎回来' : '加入星屿'" class="auth-dialog"><p>用一个账号，保存属于你的生活片段。</p><el-form @submit.prevent="authenticate"><el-form-item><el-input v-model="authForm.username" size="large" placeholder="用户名（3-32 位）" autocomplete="username" /></el-form-item><el-form-item><el-input v-model="authForm.password" size="large" type="password" placeholder="密码（至少 6 位）" show-password autocomplete="current-password" /></el-form-item><el-button type="primary" size="large" round :loading="submitting" class="full" @click="authenticate">{{ authMode === 'login' ? '登录' : '注册并登录' }}</el-button></el-form><div class="switch">{{ authMode === 'login' ? '还没有账号？' : '已经有账号？' }} <el-button text type="primary" @click="authMode = authMode === 'login' ? 'register' : 'login'">{{ authMode === 'login' ? '立即注册' : '去登录' }}</el-button></div></el-dialog>
    </div>`
}

createApp(App).use(ElementPlus).mount('#app')
