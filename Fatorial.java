public class Fatorial{	
  	// versao iterativa
	public int calcularFatorial (int n){
		int resultado = 1;
		for (int i = 2; i <= n; i++)
			resultado*=i;
		return resultado;
	}

}
