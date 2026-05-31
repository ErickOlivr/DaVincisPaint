# 🎨 DaVinciPaint

Um editor gráfico vetorial simplificado desenvolvido em **Java** que utiliza **estruturas de dados dinâmicas customizadas (implementadas do zero)** em vez de coleções nativas do Java. O projeto foca em alta eficiência de memória, manipulação vetorial e uma interface moderna que quebra o padrão visual antigo do Java Swing.

---

## Funcionalidades Principais

* **Desenho Livre Vetorial:** Captura contínua de pontos através do movimento do rato.
* **Sistema de Desfazer Nativo (Undo/Ctrl+Z):** Remoção instantânea do último traço com complexidade $O(1)$.
* **Interface High-End:** Visual minimalista em modo escuro utilizando o framework FlatLaf.
* **Customização do Pincel:** Ajuste dinâmico de cores (paleta) e espessura do traço através de sliders.
* **Gerenciamento Eficiente de Memória:** Alocação sob demanda que cresce apenas conforme o usuário desenha.

---

## Arquitetura e Estrutura de Dados

O núcleo da aplicação rejeita o uso de `ArrayList` ou `LinkedList` do Java. Em vez disso, utiliza uma **arquitetura de listas encadeadas dinâmicas aninhadas**:



1. **Lista Encadeada de Traços (Mestra):** Cada nó representa uma linha inteira e guarda propriedades como cor, espessura e o ponteiro para a sua própria lista de pontos.
2. **Lista Encadeada de Pontos (Secundária):** Armazena as coordenadas `(X, Y)` sequenciais geradas pelo arrastar do mouse.

---

## Pré-requisitos e Tecnologias

### Requisitos do Sistema
* **Java Development Kit (JDK):** Versão 17 ou superior.
* **Gerenciador de Dependências:** Maven ou inclusão manual do arquivo `.jar` do FlatLaf.

### Tecnologias Utilizadas
* **Java Swing:** Para a janela (`JFrame`), painéis (`JPanel`) e componentes de controle.
* **Java 2D (Graphics2D):** Motor de renderização com *Antialiasing* ativo e terminações de linha arredondadas (`CAP_ROUND`).
* **FlatLaf (Look and Feel):** Biblioteca open-source para modernização visual da interface gráfica.

---

## Organização do Desenvolvimento (4 Módulos)

O projeto foi segmentado em 4 partes bem definidas para facilitar o desenvolvimento em equipe:

### Parte 1: O Núcleo de Dados (Back-end)
* Implementação manual dos nós (`NoPonto` e `NoTraco`).
* Desenvolvimento da lógica de ponteiros da classe `ListaEncadeadaCustom`.
* Métodos de inserção na cauda e remoção do último elemento.

### Parte 2: O Motor Gráfico e Estética (UI/UX)
* Configuração do `FlatDarkLaf` para inicialização da interface moderna.
* Sobrescrita do método `paintComponent` no Canvas de desenho.
* Aplicação das regras de suavização geométrica do `Graphics2D`.

### Parte 3: Interação e Captura de Eventos (Controller)
* Implementação do `MouseListener` para abrir novos traços no clique.
* Implementação do `MouseMotionListener` para empilhar pontos dinamicamente durante o arraste.
* Sincronização e otimização das chamadas de `repaint()`.

### Parte 4: Sistemas de Controle e Refinamento
* Integração do botão de "Desfazer" (Undo) manipulando os ponteiros da lista mestra.
* Vinculação da paleta de cores e sliders de espessura ao estado do pincel atual.
* Sistema de limpeza completa de tela liberando referências para o *Garbage Collector*.

---

## Como Executar o Projeto

1. Clone o repositório:
   ```bash
   git clone [https://github.com/seu-usuario/modern-vector-paint.git](https://github.com/seu-usuario/modern-vector-paint.git)
   Abra o projeto na sua IDE de preferência (IntelliJ IDEA, Eclipse ou NetBeans).

Certifique-se de que a dependência do FlatLaf está configurada no seu pom.xml (caso use Maven):

    XML
    <dependency>
        <groupId>com.formdev</groupId>
        <artifactId>flatlaf</artifactId>
    <version>3.5.1</version> </dependency>
  Execute a classe principal Main.java.


## Detalhamento das Classes dentro dos Módulos
### 1. Módulo model (Estrutura de Dados)
NoPonto.java: Uma classe simples (POJO) com int x, int y e NoPonto proximo.

NoTraco.java: Contém Color cor, int espessura, um ponteiro para o primeiro ponto (NoPonto primeiraCoordenada) e um ponteiro para o próximo traço (NoTraco proximoTraco).

ListaEncadeadaCustom.java: A classe que seu professor mais vai olhar. Ela gerencia a cabeça da lista de traços. Deve conter o método adicionarTraco(NoTraco novo) e o método removerUltimoTraco() (que limpa o último nó para fazer o Undo).

### 2. Módulo view (Interface e Estética)
JanelaPrincipal.java: Constrói o layout moderno. Uma barra lateral ou superior com botões de cores, um slider (controle deslizante) para a espessura do pincel e o botão de "Desfazer".

PainelDesenho.java: É a tela em branco. Dentro dele, o método paintComponent(Graphics g) é transformado em Graphics2D g2d. Ele faz um loop while(tracoAtual != null) e, dentro dele, outro loop while(pontoAtual != null) usando g2d.drawLine() para conectar as coordenadas.

### 3. Módulo controller (Eventos e Integração)
GerenciadorMouse.java: Intercepta os movimentos. No mousePressed, ele pega a cor e espessura selecionadas na interface, cria um NoTraco e joga na lista. No mouseDragged, ele fica criando NoPonto e grudando no traço atual. A cada movimento, ele dá um canvas.repaint().

GerenciadorDesenho.java: Controla o estado global (qual cor está selecionada no momento? Qual a espessura?). Quando o botão "Desfazer" na JanelaPrincipal é clicado, este gerenciador avisa a ListaEncadeadaCustom para deletar o último traço e manda o painel se redesenhar.
