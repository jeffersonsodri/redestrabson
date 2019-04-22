# redestrabson

Redes de Computadores – 2019

Primeiro Trabalho

HTTP Proxy

## 1. Descrição do Trabalho
Neste trabalho, você implementará um proxy HTTP com cache que atuar´a entre seu navegador e a Internet.

De modo geral, HTTP é um protocolo cliente-servidor. O cliente (geralmente um navegador
web) se comunica diretamente com o servidor web. Entretanto, em algumas circunstˆancias pode
ser ´util introduzir uma entidade intermedi´aria chamada proxy. Conceitualmente, o proxy fica
entre o cliente e o servidor. No caso mais simples, ao inv´es de enviar requisi¸c˜oes diretamente
para o servidor, o cliente envia todas as suas requisi¸c˜oes para o proxy. O proxy ent˜ao abre
a conex˜ao com o servidor e repassa as requisi¸c˜oes do cliente. O proxy recebe as respostas do
servidor e as repassa para o cliente. Note que o proxy est´a atuando tanto como cliente HTTP
(para o servidor remoto) como um servidor HTTP (para o cliente inicial).
Por que usar um proxy? H´a v´arias raz˜oes poss´ıveis:
* Desempenho: ao gravar uma c´opia das p´aginas que ele busca, o proxy pode reduzir a
necessidade de se criar conex˜oes com os servidores remotos. Isso pode reduzir significativamente o tempo de resposta na recupera¸c˜ao de uma p´agina, em particular, se o servidor for distante ou estiver sob alta carga de trabalho.

* Filtragem e transforma¸c˜ao de conte´udo: o proxy pode processar o URL requisitado e
seletivamente bloquear acesso a certos dom´ınios, reformatar as p´aginas (por exemplo,
removendo imagens para tornar a visualiza¸c˜ao da p´agina mais simples para dispositivos
limitados) ou realizar outras transforma¸c˜oes ou filtragens.
* Privacidade: geralmente, servidores web armazenam todas as informa¸c˜oes de requisi¸c˜oes
que recebem. Essas informa¸c˜oes incluem tipicamente pelo menos o endere¸co IP do cliente,
o navegador, data, hora e arquivo solicitado. Se um cliente n˜ao quiser ter sua informa¸c˜ao
gravada, ele pode enviar suas requisi¸c˜oes para um proxy. Todas as requisi¸c˜oes vindas dos
clientes que usam o mesmo proxy parecem vir do mesmo endere¸co IP e agente de usu´ario
(proxy) ao inv´es dos clientes individuais. Se v´arios clientes usam o mesmo proxy (e.g.,
uma empresa ou universidade), torna-se muito dif´ıcil rastrear uma transa¸c˜ao HTTP para
um computador espec´ıfico.
Todas as requisi¸c˜oes HTTP enviadas pelo seu navegador para qualquer servidor Web deve
passar pelo seu proxy, que ir´a repassar qualquer resposta do servidor de volta para o navegador.

Se vocˆe implementar o seu proxy corretamente, e ap´os configurar o seu navegador para utilizar
o proxy, vocˆe dever´a ser capaz de navegar a Web e visualizar as p´aginas visitadas como se
vocˆe n˜ao estivesse usando o proxy. Seu programa deve ser escrito em C, C++, Go ou Java e
utilizar a biblioteca de threads da sua linguagem para criar uma nova thread para processar
cada requisi¸c˜ao que seu proxy receba.

O fluxo b´asico do seu proxy deve ser como segue:

1. Inicialize o seu proxy na linha de comandos para que ele ou¸ca em uma porta passada como
argumento na linha de comando. Seu proxy deve aceitar dois argumentos: o primeiro a
porta que ele deve escutar e o segundo o tamanho do cache em MBs.

2. No seu navegador, quando vocˆe digitar um URL como http://www.ufms.br, a requisi¸c˜ao
deve ser repassada para o seu proxy. Isso pode ser feito mudando as configura¸c˜oes do seu
navegador para que ele use um Web proxy. Vocˆe precisa especificar o endere¸co e a porta
do proxy. Feito isso, o seu navegador enviar´a todas as requisi¸c˜oes HTTP para o seu proxy.

3. Ao receber uma nova requisi¸c˜ao, o proxy deve criar uma nova thread para process´a-la.
Vocˆe precisa tratar apenas o comando GET.

4. No caso da requisi¸c˜ao ser GET, a requisi¸c˜ao inclui um URL que cont´em um host e um
caminho. O proxy deve verificar se o objeto est´a em sua cache.
(a) Se estiver em cache, o proxy deve retornar o conte´udo que est´a em cache. Note que
vocˆe ter´a de gerar os cabe¸calhos HTTP para a resposta.
(b) Se n˜ao estiver em cache, o proxy utiliza o URL para recuperar o conte´udo do servidor
de origem. Ao receber o conte´udo do servidor, o proxy repassa para o cliente e tamb´em
armazena uma c´opia em sua cache.

5. Implemente uma pol´ıtica LRU (Least Recently Used) para liberar espa¸co no cache quando
necess´ario.

6. Quando n˜ao houver mais dados a serem transferidos, feche as conex˜oes com o servidor e
com o navegador.

7. Quando o navegador fechar a conex˜ao, o proxy tamb´em deve fechar a conex˜ao com o
servidor.

# 1.1 Detalhes adicionais
Quando o proxy recebe uma requisi¸c˜ao HTTP v´alida, ele precisa analisar (parse) o URL
requisitado. O proxy precisa de pelo menos trˆes informa¸c˜oes: host requisitado, porta e caminho.
Veja a man page de URL (7) (man -s 7 URL) para mais informa¸c˜oes. Se o URL indicado n˜ao
possuir uma porta, vocˆe deve usar a porta HTTP padr˜ao 80.

Após fazer a análise do URL, o proxy pode estabelecer uma conex˜ao com o host requisitado (usando a porta remota apropriada ou a padr˜ao 80) e enviar a requisi¸c˜ao HTTP para o recurso apropriado. O proxy deve sempre enviar uma requisi¸c˜ao no formato relativo URL+Host
independentemente de como a requisi¸c˜ao foi recebida do cliente:
Aceite do cliente:
GET http://www.facom.ufms.br/ HTTP/1.0
Envie para o servidor remoto:
GET / HTTP/1.0
Host: www.facom.ufms.br
Connection: close
(Cabe¸calhos adicionais especificados pelo cliente, caso existam)

2. Entrega do Trabalho
O trabalho pode ser feito em grupo de no m´aximo dois alunos e deve ser entregue at´e
o dia 15 de abril de 2019. A entrega do trabalho consistir´a em uma demonstra¸c˜ao das funcionalidades do proxy no laborat´orio. O grupo deve preparar uma breve apresenta¸c˜ao e um
roteiro para a demonstra¸c˜ao. O grupo deve indicar explicitamente as funcionalidades que foram
implementadas e as que n˜ao foram. Na demonstra¸c˜ao, o grupo deve incluir casos que demonstrem claramente as funcionalidades implementadas, como, por exemplo, o tratamento de v´arias
conex˜oes simultˆaneas. Al´em disso, o grupo deve entregar o c´odigo fonte e um breve relat´orio
descrevendo o trabalho. Neste relat´orio, o grupo deve incluir uma breve introdu¸c˜ao, decis˜oes de
implementa¸c˜ao, funcionalidades n˜ao implementadas, problemas enfrentados na implementa¸c˜ao,
etc. O relat´orio deve ser entregue em um arquivo PDF.

3. Avaliação Além da corre¸c˜ao do programa, o professor e/ou assistente de ensino far˜ao perguntas durante
a apresenta¸c˜ao do trabalho. Durante a apresenta¸c˜ao, o grupo dever´a explicar o funcionamento
do programa e responder a perguntas relativas ao projeto.

4. Referˆencia
The Hypertext Transfer Protocol, Version 1.0. http://www.w3.org/Protocols/rfc1945/rfc1945
3
