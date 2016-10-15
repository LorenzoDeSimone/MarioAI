package competition.cig.desimonenotarangelo.ScoreEvaluatorAgent.NeuralNetwork;


//BEST LINK EVER: https://mattmazur.com/2015/03/17/a-step-by-step-backpropagation-example/
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HiddenNeuron extends Neuron
{
  protected final Set<Link> nextNeurons;
  protected final Set<Link> prevNeurons;
  
  public HiddenNeuron(double bias)
  {
    super(bias);
    nextNeurons = new HashSet<Link>();
    prevNeurons = new HashSet<Link>();
  }
  
  public void forwardPass()
  {
    Map<Neuron,Double> nets = NeuralNetwork.netsCache;
    double singleOutput = computeOutput(nets.get(this));//Calculates activation function from neuron's net sum
    for(Link link: nextNeurons)
    {
      Neuron currNext = link.getNext();
      Double nextNet = nets.get(currNext);
      //If it is the first time you add net to the neuron, the net must be initialized to 0
      if(nextNet==null)
        nextNet=0.0;
  
      nets.put(currNext, nextNet + singleOutput * link.getWeight());
    }
  }
  
  public void addPrev(Neuron prev) { prevNeurons.add(new Link(prev,this)); }
  public void addNext(Neuron next) { nextNeurons.add(new Link(this,next)); }

  public void linkToNextLayer(Set<? extends Neuron> layer) {
    for (Neuron neuron : layer)
      addNext(neuron);
  }

  public void linkToPrevLayer(Set<? extends Neuron> layer) {
      for (Neuron neuron : layer)
          addPrev(neuron);
  }
  
  protected double computeDelta()
  {
    double sum = 0;
    
    //for is for future implementations: now only one output node is supported
    for(Link link: nextNeurons)
    {
      Neuron currNext = link.getNext();
      sum +=  NeuralNetwork.deltasCache.get(currNext) * link.getWeight();
    }
    double currentNet = NeuralNetwork.netsCache.get(this);
    return ((currentNet/ (2*Math.sqrt(currentNet*currentNet+1)))+1) * sum;//Bent Identity
    /*OLD SIGMOID
    delta = output*(1-output)*sum;*/
  }
  
  public Set<Link> getPrevNeurons(){ return prevNeurons;}
  public Set<Link> getNextNeurons(){ return nextNeurons;}
  
  
}