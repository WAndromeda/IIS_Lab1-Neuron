import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Perceptron {
    private ArrayList<Neuron> inputLayer;
    private ArrayList<Neuron> firstHiddenLayer;
    private ArrayList<Neuron> outputLayer;

    private double alpha = 1.0d;
    private double learningRate = 0.5d;
    private double reluAbove = 1.0d;
    private double reluBelow = 0.001d;

    public Perceptron(int inputLayerSize, int firstHiddenSize, int outputLayerSize) {
        this.inputLayer = new ArrayList<>(inputLayerSize);
        for (int i = 0; i < inputLayerSize; i++)
            inputLayer.add(new Neuron());
        this.firstHiddenLayer = new ArrayList<>();
        this.outputLayer = new ArrayList<>();

        Random rnd = new Random();
        for (int i = 0; i < firstHiddenSize; i++) {
            this.firstHiddenLayer.add(new Neuron());
            for (int j = 0; j < inputLayerSize; j++) {
                this.firstHiddenLayer.get(i).addElemToWeights(rnd.nextDouble() * Math.pow(-1, (rnd.nextInt(2) + 1)));
            }
        }

        for (int i = 0; i < outputLayerSize; i++) {
            this.outputLayer.add(new Neuron());
            for (int j = 0; j < firstHiddenSize; j++) {
                this.outputLayer.get(i).addElemToWeights(rnd.nextDouble() * Math.pow(-1, (rnd.nextInt(2) + 1)));
            }
        }
    }

    public void printWeights(boolean when) throws IOException {
        FileWriter fw = new FileWriter(Main.mainPath + "/weights.txt");

        if (!when)
            fw.write("Before\n");
        else
            fw.write("After\n");

        fw.write("First Hidden Layer\n");

        for (Neuron neuron : firstHiddenLayer)
            for (int j = 0; j < neuron.getWeights().size(); j++)
                fw.write(neuron.getElemFromWeights(j) + "\n");

        fw.write("Output Layer\n");
        for (Neuron neuron : outputLayer)
            for (int j = 0; j < neuron.getWeights().size(); j++)
                fw.write(neuron.getElemFromWeights(j) + "\n");
        fw.close();
    }

    public double[] countResult(ArrayList<Double> input){
        int i, j;
        for (i = 0; i < input.size(); i++)
            inputLayer.get(i).setOutput(input.get(i));

        for (i = 0; i < firstHiddenLayer.size(); i++){
            firstHiddenLayer.get(i).setSum(0);
            for (j = 0; j < inputLayer.size(); j++)
                firstHiddenLayer.get(i).addToSum(inputLayer.get(j).getOutput() * firstHiddenLayer.get(i).getElemFromWeights(j));
            firstHiddenLayer.get(i).setOutput(Sigmoid(firstHiddenLayer.get(i).getSum()));
        }

        for (i = 0; i < outputLayer.size(); i++){
            outputLayer.get(i).setSum(0);
            for (j = 0; j < firstHiddenLayer.size(); j++)
                outputLayer.get(i).addToSum(firstHiddenLayer.get(j).getOutput() * outputLayer.get(i).getElemFromWeights(j));
            outputLayer.get(i).setOutput(Sigmoid(outputLayer.get(i).getSum()));
        }

        double[] result = new double[outputLayer.size()];
        for (i = 0; i < outputLayer.size(); i++)
            result[i] = outputLayer.get(i).getOutput();

        return result;
    }


    public double[] backPropagation(ArrayList<Double> input, double[] ideal){
        int i, j;
        double[] answer = countResult(input);

        for (i = 0; i < outputLayer.size(); i++)
            outputLayer.get(i).setDelta(ideal[i] - answer[i]);

        backPropagatonHelp(firstHiddenLayer, outputLayer);

        backPropagatonHelp(inputLayer, firstHiddenLayer);

        backPropagationSigmoid(firstHiddenLayer, inputLayer);

        backPropagationSigmoid(outputLayer, firstHiddenLayer);
        return answer;
    }

    private void backPropagationSigmoid(ArrayList<Neuron> firstHidden, ArrayList<Neuron> inputLayer) {
        for (int i = 0; i < firstHidden.size(); i++) {
            for (int j = 0; j < inputLayer.size(); j++){
                firstHidden.get(i).addToElemFromWeights(j, firstHidden.get(i).getDelta() *
                        SigmoidDifferential(firstHidden.get(i).getSum()) *
                        inputLayer.get(j).getOutput() * learningRate);
            }
        }
    }

    private void backPropagatonHelp(ArrayList<Neuron> firstHidden, ArrayList<Neuron> outputLayer) {
        for (int i = 0; i < firstHidden.size(); i++){
            firstHidden.get(i).setDelta(0);
            for (int j = 0; j < outputLayer.size(); j++)
                firstHidden.get(i).addToDelta(outputLayer.get(j).getDelta() * outputLayer.get(j).getElemFromWeights(i));
        }
    }

    public void resetWeights() {
        Random rnd = new Random();
        for (int i = 0; i < firstHiddenLayer.size(); i++)
            for (int j = 0; j < inputLayer.size(); j++)
                firstHiddenLayer.get(i).setElemToWeights(j, rnd.nextDouble() * Math.pow(-1, (rnd.nextInt(2) + 1)));

        for (int i = 0; i < outputLayer.size(); i++)
            for (int j = 0; j < firstHiddenLayer.size(); j++)
                outputLayer.get(i).setElemToWeights(j, rnd.nextDouble() * Math.pow(-1, (rnd.nextInt(2) + 1)));

    }

    protected double Sigmoid(double X){
        return 1.0 / (1.0 + Math.exp(-alpha * X));
    }

    protected double SigmoidDifferential(double X) {
        return Sigmoid(X) * (1.0 - Sigmoid(X));
    }

    protected double relu(double X) {
        return Math.max(X * reluAbove, X * reluBelow);
    }

    protected double reluDifferential(double X) {
        if (X > 0)
            return reluAbove;
        else{
            if (X < 0)
                return reluBelow;
            else
                return 0;
        }
    }
    public void setAlpha(double Alpha)  {
        this.alpha = Alpha;
    }

    public void setLearningRate(double lr){
        learningRate = lr;
    }

    public void setReluParams(double above, double below){
        reluAbove = above;
        reluBelow = below;
    }
}
