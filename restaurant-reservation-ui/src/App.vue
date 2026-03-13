<template>
  <header>
    <nav>
      <h1>Nutikas Restoran</h1>
    </nav>
  </header>

  <main>
    <RouterView />
  </main>
</template>

<style>
body {
  margin: 0;
  font-family: sans-serif;
  background-color: #f0f2f5;
}
header {
  background-color: #2c3e50;
  color: white;
  padding: 1rem;
}
header nav {
  text-align: center;
}
header h1 {
  margin: 0;
}
main {
  padding: 20px;
}
</style>
