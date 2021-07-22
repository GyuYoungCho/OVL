<template>
  <section>
    <div class="entire">
      <h2>페이지를 찾을 수 없습니다.</h2>
      <h1>404</h1>
      <p>잘못된 주소이거나, 다시 시도해 주세요!</p>
      <RouterLink class="a" to="/">홈으로</RouterLink>
    </div>
  </section>
</template>

<script>
export default {
  name: "404"
}
</script>

<style scoped>
  p {
    margin-bottom: 30px;
  }

  section{
    margin: 0;
    padding: 0;
    font-family: sans-serif;
    min-height: 100vh;
    background-image: linear-gradient(125deg,#6a89cc,#b8e994);
  }

  .entire{
    width: 100%;
    position: absolute;
    top: 50%;
    transform: translateY(-50%);
    text-align: center;
    color: #343434
  }

  .entire h1{
    position: relative;
    font-size: 160px;
    margin: 0;
    font-weight: 900;
    letter-spacing: 20px;
    background: url('./tree.jpg') center no-repeat;
    -webkit-text-fill-color: transparent;
    -webkit-background-clip: text;
  }

  .entire .a{
    text-decoration: none;
    background: #e55039aa;
    color: #fff;
    padding: 12px 24px;
    display: inline-block;
    border-radius: 25px;
    font-size: 14px;
    text-transform: uppercase;
    transition: 0.4s;
  }

  .entire .a:hover{
    background: #e55039;
  }
</style>