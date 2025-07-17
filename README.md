SISTEMA DE GESTÃO – VERSÃO BETA
================================

Esta é a versão beta do sistema de gestão web, desenvolvido em Java (Spring Boot) com interface web responsiva utilizando Bootstrap e HTMX.

O objetivo desta versão é permitir testes iniciais e coleta de feedback sobre o funcionamento geral da aplicação.

---

✅ FUNCIONALIDADES ATUALMENTE DISPONÍVEIS
-----------------------------------------
- Login com autenticação no banco de dados
- Cadastro e gerenciamento de:
    - Produtos
    - Compras
    - Vendas
    - Fornecedores
    - Usuários
- Tela de PDV
- Importação de dados via planilhas Excel
- Paginação com filtro por categoria e busca
- Tela de administração de usuários (com permissões)
- Relatórios de erros (400, 404, 500) e mensagens de exceção
- Exibição do usuário logado e logout
- Auditoria básica iniciada (ações de administrador)
- Estilização com Bootstrap e navegação com HTMX

---

⚠️ LIMITAÇÕES E PROBLEMAS CONHECIDOS
------------------------------------
- Algumas exceptions em requisições HTMX não redirecionam corretamente para a tela de erro
- A contagem de páginas pode não aparecer corretamente quando o filtro retorna zero resultados
- A navegação entre páginas de tabelas não funciona corretamente quando há filtros ativos
- Validações de formulário ainda serão aprimoradas
- Logs de ações de usuários ainda não implementados
- Testes automatizados ainda não foram incluídos

---

🖥️ REQUISITOS PARA EXECUÇÃO
----------------------------
- Java JDK 17 instalado
- MySQL 8 instalado e rodando
- Banco de dados configurado (ver item abaixo)

---

🛠️ CONFIGURAÇÃO DO BANCO DE DADOS
----------------------------------
1. Crie o banco no MySQL com o nome desejado (ex: `sistema_beta`)
2. Execute o script `setup.sql` fornecido para criar as tabelas e inserir dados iniciais
3. Verifique se os dados de conexão estão corretos em `application.properties` (ou configurado no .jar)

Exemplo de configuração:
```
# ========================
# CONFIGURAÇÃO DO SERVIDOR
# ========================

# Porta onde a aplicação será executada (ex: http://localhost:8080)
server.port=8080


# ========================
# CONFIGURAÇÃO DO BANCO DE DADOS
# ========================

# URL de conexão com o banco de dados MySQL
# Substitua "localhost", "3306" e "nome_do_banco" conforme necessário
spring.datasource.url=jdbc:mysql://localhost:3306/nome_do_banco?useSSL=false&serverTimezone=America/Sao_Paulo

# Usuário e senha do banco de dados
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha


# ========================
# CONFIGURAÇÃO JPA / HIBERNATE
# ========================

# Evita alterações automáticas na estrutura do banco
spring.jpa.hibernate.ddl-auto=none

# Dialeto do MySQL 8 (necessário para compatibilidade total com o banco)
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect


# ========================
# LOCALIZAÇÃO E FUSO HORÁRIO
# ========================

# Localização padrão da aplicação
spring.mvc.locale=pt_BR
spring.mvc.locale-resolver=fixed

# Fuso horário usado pela aplicação
spring.jackson.time-zone=America/Sao_Paulo
```

---

▶️ COMO EXECUTAR A APLICAÇÃO
-----------------------------
1. Abra o terminal (Prompt de Comando)
2. Vá até a pasta onde o arquivo `sistema.jar` está salvo
3. Execute o seguinte comando:
```java -jar sistema.jar```
4. Acesse o sistema pelo navegador:
http://localhost:8080
---

🔐 ACESSO INICIAL
-----------------
Usuário administrador:
- Login: admin
- Senha: admin123

---

Versão: Beta 0.1