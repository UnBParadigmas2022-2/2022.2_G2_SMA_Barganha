# Barganha

**Disciplina**: FGA0210 - PARADIGMAS DE PROGRAMAÇÃO - T01 <br>
**Nro do Grupo (de acordo com a Planilha de Divisão dos Grupos)**: 02<br>
**Paradigma**: SMA<br>

## Alunos

| Matrícula  | Aluno                             |
| ---------- | --------------------------------- |
| 19/0041871 | Abner Filipe Cunha Ribeiro        |
| 19/0102390 | André Macedo Rodrigues Alves      |
| 19/0012307 | Eduardo Afonso Dutra Silva        |
| 18/0018728 | Igor Batista Paiva                |
| 18/0033620 | João Henrique Cunha Paulino       |
| 16/0152615 | João Pedro Elias de Moura         |
| 18/0054554 | Paulo Batista                     |
| 19/0019158 | Rafael Leão Teixeira de Magalhães |
| 19/0020903 | Vitor Magalhães Lamego            |

## Sobre

### Contextualização

Em 2048 a União das Repúblicas Socialistas da América Latina (URSAL) e os
Estados Unidos da América (EUA) entraram em conflito, devido a
divergências ideológicas de sistemas econômicos. O conflito ficou
conhecido como **guerra dos roteadores**, dado que os principais alvos de
ataque foram as redes de computadores destes países.

Os ataques constantes às redes destes países causou uma
crise no gerenciamento de alimentos, combustíveis, medicamentos e itens
básicos para a sobrevivência humana. Após as agressões nas redes de
telecomunicações, as armas nucleares começaram a ser usadas
por ambos os lados, no intuito de reduzir o tempo do conflito. O que
acabou com 78% da população humana, e obrigou a parte que
restou a viver em bunkers.

![roteador](assets/router.png)

### Engenharia de Quintal

E é neste cenário apocalíptico que os estudantes da Universidade Nacional
da Bahia (UnB): Moisés, Maria, Edmilson e Edlaine estão se formando no
curso de Engenharia de Software. A comunidade científica ainda possuia
um restrito acesso à internet, através das redes federadas que conectavam
os principais centros de pesquisa da América Latina.

Os estudantes da UnB possuiam uma cota de 500 mega bits por mês de tráfego
de dados que poderiam ser usados para fins recreativos. Os estudantes
citados, em uma de suas reuniões da Empresa Júnior
**Engenharia de Quintal**, decidiram juntar suas cotas de internet para
baixar o jogo The Witcher 1, no intuito de estudar o comportamento dos
**algoritmos de barganha** implementados nos 'bruxos' para compra de livros
que os auxiliariam na criação de poções, óleos, bombas, armas, etc.
Itens quais auxiliam os 'bruxos' em seus trabalhos.

O grupo **Engenharia de Quintal** tinha muito interesse neste algoritmo
de Sistemas Multi Agentes (SMA), que havia sido implementado em C98. Dado
que se eles conseguissem extrair este código do jogo e adaptassem ele para
compradores e vendedores de livros científicos, a Universidade teria
vantagem na negociação de livros com outros institutos
de pesquisa.

![engenharia_quintal](assets/engenharia_quintal.png)

### Sobre The Witcher 1

A história de "The Witcher 1" segue o personagem Geralt de Rivia, um
caçador de monstros conhecido como "bruxo", em sua jornada para encontrar
sua identidade e destino enquanto enfrenta perigos políticos e mágicos em
um mundo de fantasia. A aventura envolve escolhas morais difíceis e se
desenrola em um contexto de guerra civil, conspiração e ameaças
sobrenaturais.

A jogabilidade de "The Witcher 1" é baseada em RPG, com elementos de ação,
aventura e decisões morais. O jogador controla Geralt em suas missões e
explorações pelo mundo, realizando tarefas, lutando contra inimigos e
tomando decisões que afetam o enredo e o resultado da história. Há um
sistema de combate dinâmico, alquimia, upgrade de habilidades, escolhas de
diálogo e um sistema de escolha de romance. O jogo se desenvolve ao longo
da escolha de missões e decisões do jogador, com resultados que
influenciam a história e o relacionamento com personagens secundários.

Em "The Witcher 1", comprar livros pode ser útil por vários motivos:

- Aumentar o conhecimento do mundo: os livros oferecem informações sobre a história, cultura, bestas e pessoas do mundo de fantasia, ampliando a compreensão do jogador do contexto em que a história se desenvolve.

- Desbloquear habilidades: alguns livros podem ensinar novas habilidades e técnicas a Geralt, melhorando suas habilidades de combate e magia.

- Completar missões: algumas missões exigem informações específicas que só podem ser obtidas através da leitura de livros.

- Aumentar a imersão: os livros fornecem uma camada adicional de profundidade e detalhes para o mundo e a história, ajudando a criar uma sensação mais imersiva de jogo.

![witcher](assets/witcher.jpg)

### Os resultados obtidos até agora

Moisés e Maria, a dupla <s>espanca código</s> especializada em engenharia
reversa, conseguiu extrair uma parte do código de **barganha**, e traduziu
em pseudo código para que os programadores, Edmilson e Edlaine,
reescrevam as adaptações, que estão na pasta [src](src/), das regras
de negócio do jogo para a realidade da UnB.

Pseudo código:

```
Barganha:
# Estruturas
    Vendedor:
		preco_inicial
		preco_minimo
	Bruxo:
        valor_pago_max
	Livro:
        Qualidade (NOVO := 3, SEMINOVO := 2, USADO := 1)


# Lógica dos agentes
	Se preco_minimo > valor_pago_max:
		Bruxo não compra.

	Se qualidade < qualidade_desejada:
		Bruxo não compra.

```

## <a id="screenshots">Screenshots</a>

### Buyer Agent

![Buyer Agent GUI](https://user-images.githubusercontent.com/71379045/215627516-906fcb32-221d-4c7b-8f69-bd96f999cf35.png)

### Seller Agent

![Seller Agent GUI](https://user-images.githubusercontent.com/71379045/215627346-e898ad72-c302-4774-8c11-546497d55b95.png)

### Gui Agent

![GUI Agent](https://user-images.githubusercontent.com/71379045/215627451-73f09314-f896-4eb5-b9a7-693c84ccd98b.png)

## Instalação

**Linguagens**: Java<br>
**Tecnologias**: Jade<br>

Para rodar este projeto, será necessário ter o Java e os binários do Jade instalados em sua máquina.

Instalação do java:

```
sudo apt install default-jre
```

Instalação do jade:

Entre no [site do Jade](https://jade.tilab.com/download/jade/license/jade-download/?x=32&y=11) e instale um dos arquivos que contenha os binários .jar do Jade.

## Uso

- Iniciar a interface do Jade com os elementos principais obrigatórios
- Iniciar pelo menos um agente vendedor
- Iniciar um agente comprador
- Preencher as informações nas interfaces e clicar nos botões de ação, conforme as imagens mostradas na Seção [Screenshots](#screenshots)
- Para mais informações veja o [vídeo da apresentação](#video)

Explique como usar seu projeto.
Procure ilustrar em passos, com apoio de telas do software, seja com base na interface gráfica, seja com base no terminal.
Nessa seção, deve-se revelar de forma clara sobre o funcionamento do software.

## <a id="video">Vídeo</a>

O vídeo da apresentação pode ser acessado através do link: https://youtu.be/easks3yufxs.

## Participações

Apresente, brevemente, como cada membro do grupo contribuiu para o projeto.
|Nome do Membro | Contribuição | Significância da Contribuição para o Projeto (Excelente/Boa/Regular/Ruim/Nula) |
| -- | -- | -- |
| Abner Filipe Cunha Ribeiro | Eu, juntamente com o Rafael e o Vitor, contribuímos para o projeto mais voltado para o lado da interface e comunicação entre os agentes. Nós criamos a interface do comprador e melhoramos a comunicação inicial entre o comprador e o vendedor, garantindo que o comprador tenha uma lista de livros disponíveis antes de escolher um para compra. Além disso, integramos nossas alterações às novas funcionalidades adicionadas pelos outros membros do grupo. Como a interface mudou a lógica do projeto, ficamos encarregados de integrar essas mudanças. Finalmente, criamos um método para adicionar livros para os sellers de forma automática, facilitando os testes. | Boa |
| André Macedo Rodrigues Alves | Minha contribuição para o trabalho foi inicialmente a possibilidade de passar a proposta como parâmetro do agente e um novo agente Thief (branch Thief-Agent) que acabou não entrando na main por causa de conflitos de última hora (já houveram muitas mudanças na reta final do projeto). Contribui também com a documentação do trabalho, explicando os passos para rodar e configurar o projeto com ilustrações. | Boa |
| Eduardo Afonso Dutra Silva | Minha contribuição foi organizar o repositório para o time poder se localizar e contribuir de forma mais fácil, além de criar a lógica de descontos no agente Seller. | Boa |
| Igor Batista Paiva | Minha contribuição geral no trabalho foi com revisões de PRs, testes das funcionalidades e edição do vídeo de apresentação. Também trabalhei na implementação da parte do atributo de qualidade dos livros. Alguns problemas encontrados foi com a configuração de IDEs (Eclipse e IntelliJ) e configuração da plataforma Jade, e também o tempo curto para realização do trabalho dado o fechamento de várias outras matérias no mesmo período. | Boa |
| João Henrique Cunha Paulino | A minha contribuição foi focada na contextualização do projeto e nos testes de algumas funcionalidades implementadas. Tive algumas dificuldades na preparação do ambiente para desenvolvimento, poderia ter me esforçado mais. | Regular |
| João Pedro Elias de Moura | Nesse projeto fiquei responsável pela parte de incluir o atributo de qualidade para fazer parte da negociação e compra de um livro. Após a negociação de preços, desenvolvida pelo meu colega de grupo, incluí a verificação para conferir se o livro que o agente vendedor possui, é igual ou segue a lógica que foi criada pelo grupo e consta na issue. Portanto, caso o comprador deseje um livro novo, ele seguirá a lógica do menor preço, mas apenas daqueles livros que são novos. No caso do comprador solicitar um livro Seminovo, ele poderá comprar os livros que são seminovos e também os novos. E por fim, caso o comprador passe como paramêtro que o livro possa ser usado, então a qualidade não interfere na compra, pois ele poderá ter o livro com qualquer uma das 3 qualidades. Como desenvolvi minha parte por último no projeto, tive que fazer algumas adaptações no código feito por meus colegas para fazer a integração. Os paramêtros que devem ser passados de qualidade devem ser: “Novo”, “Seminovo” ou “Usado”. | Boa |
| Paulo Batista | Minha contribuição foi na criação da lógica de negociação do agente Buyer com o agente Seller e integração com a lógica de descontos do agente Seller.   |
| Rafael Leão Teixeira de Magalhães | Minha contribuição no projeto foi em conjunto com o Abner e o Vitor. Fizemos a interface do agente comprador, assim como modificamos a comunicação inicial do comprador com o vendedor para que o comprador tenha a lista de livros disponíveis nos catálogos antes de escolher um para comprar. Além disso, fizemos a integração das nossas alterações com as funcionalidades novas conforme elas foram sendo inseridas pelos outros colegas de grupo. Já que com a interface a lógica de como rodar o projeto mudou bastante, acabou que ficamos responsáveis por integrar essas outras alterações às nossas modificações. E por fim, também foi realizado um método para inserir alguns livros para os sellers previamente, para não ser necessário inserir manualmente para testes. | Boa |
| Vitor Magalhães Lamego | Nesta entrega trabalhei junto do Abner e do Rafael. O trabalho principal foi em relação a criação de uma interface para o agente Buyer. Um dos pontos principais dessa criação foi a integração das variáveis que eram passadas como parâmetros para dentro da interface. Além desse trabalho na interface, foram realizadas correções em algumas outras lógicas, como algumas negociações que ficavam em loop infinito, além de não realizar nenhuma negociação quando a melhor oferta é maior do que o valor máximo que o agente deseja pagar. Por último, foi realizado um "popula" para os agentes Sellers para que seja inserido alguns Books em seu método de setup. | Boa |

## Outros

- **Lições Aprendidas**
	- A forma como é feita a comunicação entre os agentes, em um Sistema Multi Agente, deve ser algo bem definido entre os dois lados para que não exista ruído na comunicação e nas percepções de cada um.
	- Sempre é possível encontrar algum detalhe que pode ser melhorado na interação entre os agentes a fim de torná-la cada vez mais próxima do real.
- **Percepções**
	- O paradigma permite abstrair muitos detalhes de relações diversas, permitindo que o próprio tema abordado pelo grupo seja abordado de forma mais aprofundada.
	- A comunicação realizada no processo de barganha necessita de várias interações entre os agentes, caso sejam usados diversos agentes, poderia haver problema de performance.
- **Contribuições e Fragilidades**
	- O input da qualidade do livre permite escrever qualquer coisa, causando erro caso o usuário escreva uma opção inválida.
	- Existe uma fragilidade na forma como o agente Seller dá o desconto, sendo sempre descontos incrementais de 5 reais.
- **Trabalhos Futuros**
	- Substituir inputs de texto livre por selects com opções pré-estabelecidas
	- Estabelecer uma variável de estresse para a negociação em que algum lado "se cansa"
	- Realizar compras cíclicas e não encerrar o agente depois da primeira compra
	- Trabalhar com decisões baseadas em um valor monetário que o agente tem e que vai diminuindo conforme ele realiza compras.
	- Trabalhar com estoque de livro para o agente Seller, definindo descontos que podem ser dados de acordo com oferta e demanda daquele livro.

## Fontes

- Site oficial do Jade: https://jade.tilab.com/
- Tutorial de Jade com o exemplo do "Book trading": https://jade.tilab.com/doc/tutorials/JADEProgramming-Tutorial-for-beginners.pdf
