SISTEMA DE GESTÃO – VERSÃO BETA
================================

Esta é a versão beta do sistema de gestão web, desenvolvido em Java (Spring Boot) com interface web responsiva utilizando Bootstrap e HTMX.

O objetivo desta versão é permitir testes iniciais e coleta de feedback sobre o funcionamento geral da aplicação.

---
<a href="https://github.com/anri-kot/RSLauncher/releases/tag/v1.0">RSLauncher</a>
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
spring.datasource.username=root
spring.datasource.password=


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

▶️ COMO EXECUTAR A APLICAÇÃO (WINDOWS)
-----------------------------
1. Para iniciar o sistema:
   Dê um duplo clique no arquivo iniciar.bat na raiz.
   O sistema iniciará em segundo plano.
   Logs serão gravados em app/log.txt.

2. Para finalizar o sistema:
   Dê um duplo clique no arquivo parar.bat na raiz.
   O processo será encerrado corretamente.
---

🔐 ACESSO INICIAL
-----------------
Usuário administrador:
- Login: admin
- Senha: admin123

---

Versão: Beta 0.1
