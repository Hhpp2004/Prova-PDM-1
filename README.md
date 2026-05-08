# Computadores

Aplicativo Android desenvolvido em Kotlin com Jetpack Compose para cadastrar, listar, atualizar e remover clientes e computadores. Os dados são armazenados localmente em SQLite.

## Funcionalidades

- Cadastro de clientes com nome, CPF, email e telefone.
- Listagem de clientes na tela inicial.
- Visualização, atualização e remoção de clientes.
- Cadastro de computadores vinculados a um cliente.
- Listagem de computadores por cliente.
- Visualização, atualização, transferência de dono e remoção de computadores.
- Backup dos dados de clientes e computadores em arquivos `.txt`.
- Notificações locais após algumas operações de cadastro, atualização e exclusão.

## Tecnologias

- Kotlin
- Jetpack Compose
- Material 3
- Navigation Compose
- SQLite com `SQLiteOpenHelper`
- Gradle Kotlin DSL

## Estrutura Principal

```text
app/src/main/java/com/example/prova
├── Database
│   └── BancoDeDados.kt
├── Models
│   ├── Cliente.kt
│   └── Computador.kt
├── Repository
│   ├── ClienteRepository.kt
│   └── ComputadorRepository.kt
├── Service
│   ├── Backup.kt
│   └── Notificacao.kt
├── View/Components
│   ├── CadastroCliente.kt
│   ├── CadastroComputador.kt
│   ├── ListaComputadores.kt
│   ├── ScreenNavigationDrawer.kt
│   ├── TelaCliente.kt
│   └── TelaComputador.kt
└── MainActivity.kt
```

## Banco de Dados

O app usa um banco SQLite local chamado `Prova`, criado em `BancoDeDados.kt`.

Tabelas:

- `cliente`: CPF, nome, email e telefone.
- `computador`: ID, modelo, memoria RAM, preco e CPF do cliente dono.

## Backup dos Dados

O menu lateral possui duas opções de backup:

- `Backup dos dados dos computadores`: gera o arquivo `todos computadores.txt`.
- `Backup dos dados dos clientes`: gera o arquivo `todos clientes.txt`.

Os arquivos são criados na pasta pública de Downloads do dispositivo. Cada registro é salvo em uma linha, usando a representação textual dos objetos `Cliente` e `Computador`.

Quando não há dados para salvar, o app exibe uma mensagem de aviso. Quando o backup é concluído, uma notificação local informa o sucesso da operação.

## Navegação

As principais rotas do app são configuradas em `MainActivity.kt`:

```text
mainScreen
cadastroCliente
cadastroComputador
cadastroComputador/{cpf}
cliente/{cpf}
listacomp/{cpf}
computador/{id}
```

## Como Executar

1. Abra o projeto no Android Studio.
2. Aguarde a sincronização do Gradle.
3. Conecte um celular Android ou inicie um emulador.
4. Execute o app pelo Android Studio.

Também é possível compilar pelo terminal:

```bash
./gradlew :app:assembleDebug
```

Para instalar em um dispositivo conectado:

```bash
./gradlew :app:installDebug
```

## Requisitos

- Android Studio recente.
- JDK compatível com o Android Gradle Plugin usado no projeto.
- Android SDK 36 instalado.
- Dispositivo ou emulador com Android 14 ou superior, pois o `minSdk` do projeto é 34.

## Permissões

O app solicita a permissão `POST_NOTIFICATIONS` para exibir notificações locais.

## Observações

- Ao atualizar a versão do banco em `BancoDeDados.kt`, o método `onUpgrade` recria as tabelas, apagando os dados locais existentes.
