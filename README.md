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

## Descrição de métodos

#### 1. Classe NoPonto
NoPonto(int x, int y) (Construtor): Recebe as coordenadas exatas de onde o mouse passou no plano cartesiano da tela e armazena nas variáveis x e y. Ele define o ponteiro proximo como null, indicando que, ao nascer, esse ponto ainda não está conectado a nenhum outro.

#### 2. Classe NoTraco
NoTraco(Color cor, int espessura) (Construtor): Cria um novo traço vazio. Ele define as propriedades estéticas daquela linha específica (cor e tamanho do pincel) e inicializa as referências dos pontos internos (cabecaPontos e caudaPontos) como null, além de preparar o ponteiro proximoTraco para quando um novo traço for feito depois dele.

adicionarPonto(int x, int y): Este método é disparado continuamente enquanto o usuário arrasta o mouse.

Se for o primeiro ponto do clique, ele define esse ponto como a cabeca e a cauda da lista interna.

Se o usuário já estiver arrastando, ele usa o ponteiro caudaPontos para grudar o novo ponto diretamente no final da linha (complexidade O(1), ou seja, instantâneo), e atualiza a cauda para ser esse novo ponto.

#### 3. Classe ListaEncadeadaCustom
ListaEncadeadaCustom() (Construtor): Inicializa a estrutura do Canvas (tela) vazia, definindo o primeiro traço (cabecaTracos) como null.

adicionarTraco(NoTraco novoTraco): Chamado no momento exato em que o usuário clica com o mouse na tela para iniciar um novo desenho. O método verifica se a tela está totalmente em branco; se estiver, o novoTraco vira a cabecaTracos. Caso contrário, ele faz uma varredura na lista através de um laço while até encontrar o último traço desenhado e conecta o novo traço logo após ele.

removerUltimoTraco() (O método do Undo / Desfazer): É o método que apaga a última ação do usuário.

Ele valida se há traços na tela. Se houver apenas um, ele limpa a cabecaTracos definindo-a como null.

Se houver vários traços, ele percorre a lista até achar o penúltimo traço. Ao encontrá-lo, ele altera o ponteiro do penúltimo para null. O último traço perde a referência no sistema e o Garbage Collector do Java se encarrega de deletá-lo da memória RAM.

limpar(): Zera a tela inteira instantaneamente. Ele simplesmente corta a referência da cabecaTracos mudando-a para null. Como a interface perde o rastro de onde a lista começava, todos os traços subsequentes são descartados da memória.

#### 4.Classe PainelDesenho (O Canvas de Desenho)
Esta classe estende um JPanel do Swing e funciona como a "tela em branco" do Paint. É ela quem lê a sua lista encadeada e transforma os nós em linhas visíveis.

PainelDesenho(ListaEncadeadaCustom lista) (Seu Construtor): Recebe a referência da lista mestra de traços criada no núcleo do programa. Ele define que o fundo do painel será escuro (combinando com o Dark Mode) e inicializa as configurações básicas da área de desenho.

paintComponent(Graphics g) (O Motor de Renderização): Este é o método mais importante do visor, chamado automaticamente pelo Java sempre que a tela precisa ser atualizada (via repaint()).

Conversão para Graphics2D: Ele transforma o objeto Graphics nativo em Graphics2D para liberar recursos avançados de desenho.

Suavização (Antialiasing): Configura filtros geométricos para eliminar o efeito serrilhado dos traços, deixando as linhas perfeitamente lisas.

Acabamento Arredondado (BasicStroke): Define que as linhas terão pontas e junções arredondadas. Isso é essencial no desenho livre, pois impede que as curvas fiquem "quadradas" ou quebradas.

O Laço de Varredura (Desenho dos Nós): Ele inicia um loop que começa na cabecaTracos. Para cada traço encontrado, ele muda a cor e a espessura do pincel do sistema e entra em um segundo loop interno, percorrendo a lista de pontos daquele traço e desenhando uma linha reta ligando o pontoAtual ao proximoPonto. Ele faz isso até que todos os traços e pontos sejam desenhados na tela.

#### 5. Classe JanelaPrincipal (A Moldura do Aplicativo)
Esta classe estende um JFrame e funciona como a janela principal do software. Ela organiza onde ficam os botões de controle e onde fica a área de desenho.

JanelaPrincipal() (Seu Construtor): Define o título da janela, o tamanho padrão do programa na tela (ex: 1200x800 pixels) e o comportamento de fechar o processo ao clicar no "X". Ele também inicializa as instâncias da estrutura de dados e dos gerenciadores.

inicializarComponentes(): Método responsável por montar e organizar o layout visual (geralmente usando um BorderLayout).

Ele cria e posiciona a Barra de Ferramentas (Toolbar) no topo ou na lateral.

Adiciona o painel de desenho (PainelDesenho) no centro da tela.

criarBarraFerramentas(): Um método auxiliar que fabrica os botões e seletores da interface. Ele adiciona:

Botões de cores (Paleta de Cores).

Um Slider (controle deslizante) para o usuário escolher a espessura do pincel em tempo real.

O botão de Desfazer (Undo) e o botão de Limpar Tela.

Cada um desses botões recebe um ouvinte de clique (ActionListener) que avisa o sistema sobre qual ação executar.

#### 6.Classe PainelDesenho (O Canvas de Desenho)
Esta classe estende um JPanel do Swing e funciona como a "tela em branco" do Paint. É ela quem lê a sua lista encadeada e transforma os nós em linhas visíveis.

PainelDesenho(ListaEncadeadaCustom lista) (Seu Construtor): Recebe a referência da lista mestra de traços criada no núcleo do programa. Ele define que o fundo do painel será escuro (combinando com o Dark Mode) e inicializa as configurações básicas da área de desenho.

paintComponent(Graphics g) (O Motor de Renderização): Este é o método mais importante do visor, chamado automaticamente pelo Java sempre que a tela precisa ser atualizada (via repaint()).

Conversão para Graphics2D: Ele transforma o objeto Graphics nativo em Graphics2D para liberar recursos avançados de desenho.

Suavização (Antialiasing): Configura filtros geométricos para eliminar o efeito serrilhado dos traços, deixando as linhas perfeitamente lisas.

Acabamento Arredondado (BasicStroke): Define que as linhas terão pontas e junções arredondadas. Isso é essencial no desenho livre, pois impede que as curvas fiquem "quadradas" ou quebradas.

O Laço de Varredura (Desenho dos Nós): Ele inicia um loop que começa na cabecaTracos. Para cada traço encontrado, ele muda a cor e a espessura do pincel do sistema e entra em um segundo loop interno, percorrendo a lista de pontos daquele traço e desenhando uma linha reta ligando o pontoAtual ao proximoPonto. Ele faz isso até que todos os traços e pontos sejam desenhados na tela.

#### 7. Classe JanelaPrincipal (A Moldura do Aplicativo)
Esta classe estende um JFrame e funciona como a janela principal do software. Ela organiza onde ficam os botões de controle e onde fica a área de desenho.

JanelaPrincipal() (Seu Construtor): Define o título da janela, o tamanho padrão do programa na tela (ex: 1200x800 pixels) e o comportamento de fechar o processo ao clicar no "X". Ele também inicializa as instâncias da estrutura de dados e dos gerenciadores.

inicializarComponentes(): Método responsável por montar e organizar o layout visual (geralmente usando um BorderLayout).

Ele cria e posiciona a Barra de Ferramentas (Toolbar) no topo ou na lateral.

Adiciona o painel de desenho (PainelDesenho) no centro da tela.

criarBarraFerramentas(): Um método auxiliar que fabrica os botões e seletores da interface. Ele adiciona:

Botões de cores (Paleta de Cores).

Um Slider (controle deslizante) para o usuário escolher a espessura do pincel em tempo real.

O botão de Desfazer (Undo) e o botão de Limpar Tela.

Cada um desses botões recebe um ouvinte de clique (ActionListener) que avisa o sistema sobre qual ação executar.
