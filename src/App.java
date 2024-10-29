import java.util.Scanner;

// Clase base del personaje
class Personaje {
    String nombre;
    int fuerza;
    int velocidad;
    int vida_hp;
    boolean defendiendo;

    public Personaje(String nombre, int fuerza, int velocidad, int vida_hp) {
        this.nombre = nombre;
        this.fuerza = fuerza;
        this.velocidad = velocidad;
        this.vida_hp = vida_hp;
        this.defendiendo = false;
    }

    //atacar a un oponente
    public void atacar(Personaje oponente) {
        int damage = this.fuerza;
        if (oponente.defendiendo) {
            damage -= 10; // Resta 10 puntos de daño si el oponente se está defendiendo
            if (damage < 0) damage = 0; // Asegura que el daño no sea negativo
        }
        oponente.vida_hp -= damage;
        System.out.println(this.nombre + " ataca a " + oponente.nombre + " causando " + damage + " puntos de daño.");
    }

    //defenderse
    public void defender() {
        this.defendiendo = true;
        this.vida_hp += 10; 
        System.out.println(this.nombre + " se está defendiendo.");
    }

    //estadísticas actuales
    public void mostrarEstadisticas() {
        System.out.println(this.nombre + " | Vida: " + this.vida_hp + " | Fuerza: " + this.fuerza + " | Velocidad: " + this.velocidad);
    }

    // Método para reducir cooldowns (se sobrescribe en subclases si es necesario)
    public void reducirCooldown() {
        // Sin implementación en la clase base
    }
}

// Clase SuperHero hereda de Personaje
class SuperHero extends Personaje {
    private int cooldownLlamarada = 0;
    private boolean transformado = false;

    public SuperHero(String nombre, int fuerza, int velocidad, int vida_hp) {
        super(nombre, fuerza, velocidad, vida_hp);
    }

    // Llamarada solar
    public void llamaradaSolar(Personaje oponente) {
        if (cooldownLlamarada == 0) {
            int damage = 45;
            if (oponente.defendiendo) {
                damage -= 10; // Resta 10 puntos de daño si el oponente se está defendiendo
                if (damage < 0) damage = 0;
            }
            oponente.vida_hp -= damage;
            cooldownLlamarada = 2;
            System.out.println(this.nombre + " usa Llamarada Solar contra " + oponente.nombre + ", causando " + damage + " puntos de daño.");
        } else {
            System.out.println("¡Llamarada Solar está en cooldown por " + cooldownLlamarada + " turnos más!");
        }
    }

    // Transformarse
    public void transformarse() {
        if (!transformado) {
            this.vida_hp += 30;
            this.fuerza += 10;
            this.velocidad += 10;
            System.out.println("Soy el guerrero del que has oído hablar en las leyendas. ¡Soy el Super Saiyan!" + "El mostacho se a transformado, es tu fin " );
            transformado = true;
        } else {
            System.out.println("Ya estás transformado.");
        }
    }

    // Reducir cooldown de habilidades
    @Override
    public void reducirCooldown() {
        if (cooldownLlamarada > 0) cooldownLlamarada--;
    }
}

// Clase Villano hereda de Personaje
class Villano extends Personaje {
    private int cooldownMakankosapo = 0;
    private int auraOscuraUsos = 3;

    public Villano(String nombre, int fuerza, int velocidad, int vida_hp) {
        super(nombre, fuerza, velocidad, vida_hp);
    }

    // Makankosapo
    public void makankosapo(Personaje oponente) {
        if (cooldownMakankosapo == 0) {
            int damage = 55;
            if (oponente.defendiendo) {
                damage -= 10; // Resta 10 puntos de daño si el oponente se está defendiendo
                if (damage < 0) damage = 0;
            }
            oponente.vida_hp -= damage;
            cooldownMakankosapo = 3;
            System.out.println(this.nombre + " usa Makankosapo contra " + oponente.nombre + ", causando " + damage + " puntos de daño.");
        } else {
            System.out.println("¡Makankosapo está en cooldown por " + cooldownMakankosapo + " turnos más!");
        }
    }

    // Aura oscura
    public void auraOscura() {
        if (auraOscuraUsos > 0) {
            this.vida_hp += 10;
            this.velocidad += 5;
            auraOscuraUsos--;
            System.out.println(this.nombre + " usa Aura Oscura, curándose 10 puntos y aumentando su velocidad en 5.");
        } else {
            System.out.println("No puedes usar más Aura Oscura.");
        }
    }

    // Reducir cooldown de habilidades
    @Override
    public void reducirCooldown() {
        if (cooldownMakankosapo > 0) cooldownMakankosapo--;
    }
}

// Clase principal para ejecutar el combate
public class App {
    public static void main(String[] args) {
        SuperHero heroe = new SuperHero("ElMostachoSolar", 30, 30, 110);
        Villano villano = new Villano("Majunia", 35, 25, 120);

        Scanner sc = new Scanner(System.in);
        String accionHeroe = "";
        String accionVillano = "";

        while (heroe.vida_hp > 0 && villano.vida_hp > 0) {
            System.out.println("\n--- Turno actual ---");
            heroe.mostrarEstadisticas();
            villano.mostrarEstadisticas();

            // El héroe selecciona su acción
            System.out.println("\nAcción del Héroe:");
            System.out.println("1. Atacar\n2. Llamarada Solar\n3. Defender\n4. Transformarse");
            int eleccionHeroe = sc.nextInt();
            accionHeroe = seleccionarAccion(eleccionHeroe, heroe);

            // El villano selecciona su acción
            System.out.println("\nAcción del Villano:");
            System.out.println("1. Atacar\n2. Makankosapo\n3. Defender\n4. Aura Oscura");
            int eleccionVillano = sc.nextInt();
            accionVillano = seleccionarAccion(eleccionVillano, villano);

            // Ejecutar acciones basadas en la velocidad
            if (heroe.velocidad > villano.velocidad) {
                ejecutarAccion(accionHeroe, heroe, villano);
                if (villano.vida_hp > 0) ejecutarAccion(accionVillano, villano, heroe);
            } else {
                ejecutarAccion(accionVillano, villano, heroe);
                if (heroe.vida_hp > 0) ejecutarAccion(accionHeroe, heroe, villano);
            }

            // Reducir cooldowns
            heroe.reducirCooldown();
            villano.reducirCooldown();

            // Reiniciar defensa después de ejecutar acciones
            heroe.defendiendo = false;
            villano.defendiendo = false;
        }

        // Determinar el ganador
        if (heroe.vida_hp > 0) {
            System.out.println(heroe.nombre + " ha ganado la batalla.");
        } else {
            System.out.println(villano.nombre + " ha ganado la batalla.");
        }

        sc.close();
    }

    // Método para seleccionar acciones
    public static String seleccionarAccion(int eleccion, Personaje personaje) {
        switch (eleccion) {
            case 1:
                return "atacar";
            case 2:
                if (personaje instanceof SuperHero) {
                    return "llamaradaSolar";
                } else {
                    return "makankosapo";
                }
            case 3:
                personaje.defender();
                System.out.println(personaje.nombre + " se está defendiendo."); // Añadido
                return "defender";
            case 4:
                if (personaje instanceof SuperHero) {
                    return "transformarse";
                } else {
                    return "auraOscura";
                }
            default:
                System.out.println("Opción no válida.");
                return "";
        }
    }

    // Método para ejecutar las acciones seleccionadas
    public static void ejecutarAccion(String accion, Personaje atacante, Personaje oponente) {
        switch (accion) {
            case "atacar":
                atacante.atacar(oponente);
                break;
            case "llamaradaSolar":
                ((SuperHero) atacante).llamaradaSolar(oponente);
                break;
            case "makankosapo":
                ((Villano) atacante).makankosapo(oponente);
                break;
            case "defender":
                // Ya se maneja en seleccionarAccion
                break;
            case "transformarse":
                ((SuperHero) atacante).transformarse();
                break;
            case "auraOscura":
                ((Villano) atacante).auraOscura();
                break;
            default:
                System.out.println("Acción no válida.");
        }
    }
}
