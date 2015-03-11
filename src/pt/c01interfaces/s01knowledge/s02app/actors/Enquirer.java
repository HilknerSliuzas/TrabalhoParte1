package pt.c01interfaces.s01knowledge.s02app.actors;

import java.util.ArrayList;
import java.util.List;

import pt.c01interfaces.s01knowledge.s01base.impl.BaseConhecimento;
import pt.c01interfaces.s01knowledge.s01base.inter.IBaseConhecimento;
import pt.c01interfaces.s01knowledge.s01base.inter.IDeclaracao;
import pt.c01interfaces.s01knowledge.s01base.inter.IEnquirer;
import pt.c01interfaces.s01knowledge.s01base.inter.IObjetoConhecimento;
import pt.c01interfaces.s01knowledge.s01base.inter.IResponder;

public class Enquirer implements IEnquirer
{
    IObjetoConhecimento obj;
    
    /* Criando lista que armazenara as perguntas ja feitas e suas
     * respectivas respostas */
    List<String> Perguntas = new ArrayList<String>();
    List<String> Respostas = new ArrayList<String>();
	
	public Enquirer()
	{
	}
	
	
	@Override
	public void connect(IResponder responder)
	{
        IBaseConhecimento bc = new BaseConhecimento();
		
        /* Obtem a lista dos animais da base de dados */
        String listaAnimais[] = bc.listaNomes();
        
        /* Quando o animal pensado for descoberto acertei se torna verdadeiro */
        boolean acertei = false;
    	
        /* decl guarda as perguntas e respostas sobre o animal chutado pelo
         * entrevistador */
		IDeclaracao decl;
        
		/* Descobrindo qual o animal pensado pelo entrevistado */
        for (int animal = 0; (!acertei) && (animal < listaAnimais.length); animal++) {
			obj = bc.recuperaObjeto(listaAnimais[animal]);
			
			/* Obtendo informacoes da primeira pergunta referente ao animal */
			decl = obj.primeira();
			
			/* animalEsperado é utilizado como flag. Caso o animal esperado nao seja
	         * o animal pensado seu valor se torna falso para sair do loop e tentar
	         * outro animal da lista */
	        boolean animalEsperado = true;
	        
	        /* Analisando todas as perguntas referentes ao animal esperado */
			while ((decl != null) && (animalEsperado)) {
				String pergunta = decl.getPropriedade();
				String respostaEsperada = decl.getValor();
				
				boolean jaRespondeu = false;
				int cont;
				
				/* Analisando se a proxima pergunta referente ao animal ja foi feita
				 * anteriormente */
				for (cont = 0; cont < Perguntas.size(); cont++) {
					if (Perguntas.get(cont).equalsIgnoreCase(pergunta)) {
						jaRespondeu = true;
						break;
					}
				}
				
				/* Adicionando a pergunta feita e sua resposta as listas */
				if (jaRespondeu == false) {
					String resposta = responder.ask(pergunta);
					Perguntas.add(pergunta);
					Respostas.add(respostaEsperada);
					
					/* Observando se a resposta dada eh a mesma que a resposta esperada */
					if (resposta.equalsIgnoreCase(respostaEsperada))
						decl = obj.proxima();
					else
						animalEsperado = false;
				}
				
				/* Analisando o caso de a pergunta ja ter sido feita */
				else {
					if (Respostas.get(cont).equalsIgnoreCase(respostaEsperada))
						decl = obj.proxima();
					else
						animalEsperado = false;
				}
			}
			
			/* Sendo o animal esperado o mesmo que o animal pensado pelo entrevistador,
			 * esse animal é dado como resposta final */
			if (animalEsperado == true)
				acertei = responder.finalAnswer(listaAnimais[animal]);
		
        }
        
        /* Analisando os casos de ter acetado ou nao a resposta final */
		if (acertei)
			System.out.println("Oba! Acertei!");
		else
			System.out.println("fuem! fuem! fuem!");
        
	}

}
