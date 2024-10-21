import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

class ConversorMoneda {

    // URL de la API con una API key (necesitas reemplazar con la tuya)
    private static final String API_KEY = "69060ae16f5efc3f26a4b50e"; // Clave de la API aquí
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/USD";

    // Método para realizar una solicitud HTTP a la API de tasas de cambio
    private static JsonObject obtenerTasasCambio() {
        try {
            // Crear cliente HTTP
            HttpClient client = HttpClient.newHttpClient();

            // Crear solicitud HTTP
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .build();

            // Enviar la solicitud y obtener la respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Analizar la respuesta JSON
            return JsonParser.parseString(response.body()).getAsJsonObject();
        } catch (Exception e) {
            System.out.println("Error al obtener las tasas de cambio: " + e.getMessage());
            return null;
        }
    }

    // Método para realizar la conversión de moneda
    private static double convertirMoneda(double cantidad, double tasaCambio) {
        return cantidad * tasaCambio;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        // Obtener las tasas de cambio desde la API
        JsonObject tasas = obtenerTasasCambio();
        if (tasas == null) {
            System.out.println("No se pudo obtener las tasas de cambio. Saliendo...");
            return;
        }

        // Obtener las tasas de cambio específicas
        JsonObject rates = tasas.getAsJsonObject("conversion_rates");
        double tasaUSDToARS = rates.get("ARS").getAsDouble();
        double tasaUSDToBRL = rates.get("BRL").getAsDouble();
        double tasaUSDToCOP = rates.get("COP").getAsDouble();
        double tasaARSToUSD = 1 / tasaUSDToARS;
        double tasaBRLToUSD = 1 / tasaUSDToBRL;
        double tasaCOPToUSD = 1 / tasaUSDToCOP;

        // Bucle del menú
        while (!salir) {
            System.out.println("******************************************");
            System.out.println("--- Bienvenid@ al Conversor de Moneda ---");
            System.out.println("\n1) Dólar =>> Peso argentino");
            System.out.println("2) Peso argentino =>> Dólar");
            System.out.println("3) Dólar =>> Real brasilero");
            System.out.println("4) Real brasilero =>> Dólar");
            System.out.println("5) Dólar =>> Peso colombiano");
            System.out.println("6) Peso colombiano =>> Dólar");
            System.out.println("7) Salir");
            System.out.print("Elija una opción válida: ");
            System.out.println("\n******************************************");

            int opcion = scanner.nextInt();

            if (opcion >= 1 && opcion <= 6) {
                System.out.print("Ingrese la cantidad: ");
                double cantidad = scanner.nextDouble();
                double resultado = 0.0;

                switch (opcion) {
                    case 1:
                        resultado = convertirMoneda(cantidad, tasaUSDToARS);
                        System.out.printf("%.2f Dólar =>> %.2f Peso argentino\n", cantidad, resultado);
                        break;
                    case 2:
                        resultado = convertirMoneda(cantidad, tasaARSToUSD);
                        System.out.printf("%.2f Peso argentino =>> %.2f Dólar\n", cantidad, resultado);
                        break;
                    case 3:
                        resultado = convertirMoneda(cantidad, tasaUSDToBRL);
                        System.out.printf("%.2f Dólar =>> %.2f Real brasilero\n", cantidad, resultado);
                        break;
                    case 4:
                        resultado = convertirMoneda(cantidad, tasaBRLToUSD);
                        System.out.printf("%.2f Real brasilero =>> %.2f Dólar\n", cantidad, resultado);
                        break;
                    case 5:
                        resultado = convertirMoneda(cantidad, tasaUSDToCOP);
                        System.out.printf("%.2f Dólar =>> %.2f Peso Colombiano\n", cantidad, resultado);
                        break;
                    case 6:
                        resultado = convertirMoneda(cantidad, tasaCOPToUSD);
                        System.out.printf("%.2f Peso Colombiano =>> %.2f Dólar\n", cantidad, resultado);
                        break;
                }
            } else if (opcion == 7) {
                salir = true;
                System.out.println("Saliendo...");
            } else {
                System.out.println("Opción no válida.");
            }
        }

        scanner.close();
    }
}
