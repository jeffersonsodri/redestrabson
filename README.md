# redestrabson
### package

Redes de Computadores – 2019

Primeiro Trabalho

HTTP Proxy

## 1. Descrição do Trabalho
Neste trabalho, você implementará um proxy HTTP com cache que atuará entre seu navegador e a Internet.

De modo geral, HTTP é um protocolo cliente-servidor. O cliente (geralmente um navegador
web) se comunica diretamente com o servidor web. Entretanto, em algumas circunstˆancias pode
ser útil introduzir uma entidade intermediária chamada proxy. Conceitualmente, o proxy fica
entre o cliente e o servidor. No caso mais simples, ao invés de enviar requisições diretamente
para o servidor, o cliente envia todas as suas requisições para o proxy. O proxy então abre
a conexão com o servidor e repassa as requisições do cliente. O proxy recebe as respostas do
servidor e as repassa para o cliente. Note que o proxy está atuando tanto como cliente HTTP
(para o servidor remoto) como um servidor HTTP (para o cliente inicial).
Por que usar um proxy? Há várias razões possíveis:
* Desempenho: ao gravar uma cópia das páginas que ele busca, o proxy pode reduzir a
necessidade de se criar conexões com os servidores remotos. Isso pode reduzir significativamente o tempo de resposta na recuperação de uma página, em particular, se o servidor for distante ou estiver sob alta carga de trabalho.

* Filtragem e transformação de conteúdo: o proxy pode processar o URL requisitado e
seletivamente bloquear acesso a certos domínios, reformatar as páginas (por exemplo,
removendo imagens para tornar a visualização da página mais simples para dispositivos
limitados) ou realizar outras transformações ou filtragens.

* Privacidade: geralmente, servidores web armazenam todas as informações de requisições
que recebem. Essas informações incluem tipicamente pelo menos o endereço IP do cliente,
o navegador, data, hora e arquivo solicitado. Se um cliente não quiser ter sua informação
gravada, ele pode enviar suas requisições para um proxy. Todas as requisições vindas dos
clientes que usam o mesmo proxy parecem vir do mesmo endereço IP e agente de usuário
(proxy) ao invés dos clientes individuais. Se vários clientes usam o mesmo proxy (e.g.,
uma empresa ou universidade), torna-se muito difícil rastrear uma transação HTTP para
um computador específico.

Todas as requisições HTTP enviadas pelo seu navegador para qualquer servidor Web deve
passar pelo seu proxy, que irá repassar qualquer resposta do servidor de volta para o navegador.

Se você implementar o seu proxy corretamente, e após configurar o seu navegador para utilizar
o proxy, você deverá ser capaz de navegar a Web e visualizar as páginas visitadas como se
você não estivesse usando o proxy. Seu programa deve ser escrito em C, C++, Go ou Java e
utilizar a biblioteca de threads da sua linguagem para criar uma nova thread para processar
cada requisição que seu proxy receba.

* O fluxo básico do seu proxy deve ser como segue:

1. Inicialize o seu proxy na linha de comandos para que ele ouça em uma porta passada como
argumento na linha de comando. Seu proxy deve aceitar dois argumentos: o primeiro a
porta que ele deve escutar e o segundo o tamanho do cache em MBs.

2. No seu navegador, quando você digitar um URL como http://www.ufms.br, a requisição
deve ser repassada para o seu proxy. Isso pode ser feito mudando as configurações do seu
navegador para que ele use um Web proxy. Você precisa especificar o endereço e a porta
do proxy. Feito isso, o seu navegador enviará todas as requisições HTTP para o seu proxy.

3. Ao receber uma nova requisição, o proxy deve criar uma nova thread para processá-la.
Você precisa tratar apenas o comando GET.

4. No caso da requisição ser GET, a requisição inclui um URL que contém um host e um
caminho. O proxy deve verificar se o objeto está em sua cache.
(a) Se estiver em cache, o proxy deve retornar o conteúdo que está em cache. Note que
você terá de gerar os cabeçalhos HTTP para a resposta.
(b) Se não estiver em cache, o proxy utiliza o URL para recuperar o conteúdo do servidor
de origem. Ao receber o conteúdo do servidor, o proxy repassa para o cliente e também
armazena uma cópia em sua cache.

5. Implemente uma política LRU (Least Recently Used) para liberar espaço no cache quando
necessário.

6. Quando não houver mais dados a serem transferidos, feche as conexões com o servidor e
com o navegador.

7. Quando o navegador fechar a conexão, o proxy também deve fechar a conexão com o
servidor.

## 1.1 Detalhes adicionais
Quando o proxy recebe uma requisição HTTP válida, ele precisa analisar (parse) o URL
requisitado. O proxy precisa de pelo menos três informações: host requisitado, porta e caminho.
Veja a man page de URL (7) (man -s 7 URL) para mais informações. Se o URL indicado não
possuir uma porta, você deve usar a porta HTTP padrão 80.

Após fazer a análise do URL, o proxy pode estabelecer uma conexão com o host requisitado (usando a porta remota apropriada ou a padrão 80) e enviar a requisição HTTP para o recurso apropriado. O proxy deve sempre enviar uma requisição no formato relativo URL+Host
independentemente de como a requisição foi recebida do cliente:
Aceite do cliente:

 > GET http://www.facom.ufms.br/ HTTP/1.0
 
 > Envie para o servidor remoto:
 
 > GET / HTTP/1.0
 
 > Host: www.facom.ufms.br
 
 > Connection: close
 
 > (Cabeçalhos adicionais especificados pelo cliente, caso existam)

1. Entrega do Trabalho
O trabalho pode ser feito em grupo de no máximo dois alunos e deve ser entregue até
o dia 15 de abril de 2019. A entrega do trabalho consistirá em uma demonstração das funcionalidades do proxy no laboratório. O grupo deve preparar uma breve apresentação e um
roteiro para a demonstração. O grupo deve indicar explicitamente as funcionalidades que foram
implementadas e as que não foram. Na demonstração, o grupo deve incluir casos que demonstrem claramente as funcionalidades implementadas, como, por exemplo, o tratamento de várias
conexões simultˆaneas. Além disso, o grupo deve entregar o código fonte e um breve relatório
descrevendo o trabalho. Neste relatório, o grupo deve incluir uma breve introdução, decisões de
implementação, funcionalidades não implementadas, problemas enfrentados na implementação,
etc. O relatório deve ser entregue em um arquivo PDF.

2. Avaliação Além da correção do programa, o professor e/ou assistente de ensino farão perguntas durante
a apresentação do trabalho. Durante a apresentação, o grupo deverá explicar o funcionamento
do programa e responder a perguntas relativas ao projeto.

3. Referência
The Hypertext Transfer Protocol, Version 1.0. http://www.w3.org/Protocols/rfc1945/rfc1945
