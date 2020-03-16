import java.util.ArrayList;

public class Neuron {
    private double output;
    private double sum;
    private double delta;
    private ArrayList<Double> weights;

    public Neuron() {
        weights = new ArrayList<>();
    }

    public Neuron(double output, double sum, double delta, ArrayList<Double> weights) {
        this.output = output;
        this.sum = sum;
        this.delta = delta;
        this.weights = new ArrayList<>(weights);
    }

    public double getOutput() {
        return output;
    }

    public void setOutput(double output) {
        this.output = output;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    public ArrayList<Double> getWeights() {
        return weights;
    }

    public void addToSum(double value){
        sum += value;
    }

    public void addToDelta(double value){
        delta += value;
    }

    public void setElemToWeights(int index, double value){
        weights.set(index, value);
    }

    public void addToElemFromWeights(int index, double value){
        weights.set(index, weights.get(index)+value);
    }

    public void addElemToWeights(double value){
        weights.add(value);
    }

    public Double getElemFromWeights(int index){
        return weights.get(index);
    }

    public void deleteElemWeights(int index){
        weights.remove(index);
    }

    public void setWeights(ArrayList<Double> weights) {
        this.weights = weights;
    }
}
